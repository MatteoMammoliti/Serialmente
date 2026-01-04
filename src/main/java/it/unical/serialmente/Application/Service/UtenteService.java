package it.unical.serialmente.Application.Service;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Utente;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.CredenzialiUtenteDAOPostgres;
import it.unical.serialmente.TechnicalServices.Persistence.dao.postgres.UtenteDAOPostgres;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.SQLException;

public class UtenteService {
    private final UtenteDAO utenteDao = new UtenteDAOPostgres(DBManager.getInstance().getConnection());
    private final CredenzialiUtenteDAO credenzialiUtente = new CredenzialiUtenteDAOPostgres(DBManager.getInstance().getConnection());


    /**
     * Dato l'id, restituisce il nome dell'utente.
     * @return nome dell'utente associato all'id della sessione corrente.
     */
    public String getNomeUtente(){
        return utenteDao.getNomeUtente(SessioneCorrente.getUtenteCorrente().getIdUtente());
    }

    /**
     * Registra l'utente all'interno dell'applicazione, prendendo tutte le informazioni necessarie all'interno
     * dell'oggetto Utente.
     * @param "nome,email,password,domanda sicurezza e risposta domnda sicurezza"
     * @return valore booleano per confermare o meno l'operazione.
     */
    public boolean registraUtente(String nome,
                                  String email,
                                  String password,
                                  String domandaSicurezza,
                                  String rispostaDomandaSicurezza) throws SQLException {

        try{
            DBManager.getInstance().getConnection().setAutoCommit(false);
            boolean emailtrovata;
            boolean inserimentoRiuscito;
            emailtrovata= credenzialiUtente.cercaEmail(email);

            if(emailtrovata){
                DBManager.getInstance().getConnection().rollback();
                return false;
            }

            int inserisciUtente= utenteDao.inserisciUtente(nome);

            if(inserisciUtente==0){
                DBManager.getInstance().getConnection().rollback();
                return false;
            }

            String pwcriptata = BCrypt.hashpw(password,BCrypt.gensalt(12));
            String rispostaCriptata = BCrypt.hashpw(rispostaDomandaSicurezza,BCrypt.gensalt(12));

            inserimentoRiuscito=credenzialiUtente.insertCredenzialiUtente(
                    email,
                    pwcriptata,
                    domandaSicurezza,
                    rispostaCriptata,
                    inserisciUtente
            );

            if(!inserimentoRiuscito){
                DBManager.getInstance().getConnection().rollback();
                return false;
            }

            DBManager.getInstance().getConnection().commit();
            return true;
        }catch (Exception e){
            DBManager.getInstance().getConnection().rollback();
            throw new RuntimeException(e.getMessage());
        }finally {
            DBManager.getInstance().getConnection().setAutoCommit(true);
        }
    }

    /**
     * Permette di convalidare o meno l'autenticazione dell'utente nella piattaforma.
     * @param email Email dell'utente
     * @param password Password dell'utente.
     * @return L'oggetto dell'Utente in questione, in caso di insucesso NULL.
     */
    public Utente loginUtente(String email, String password){
        if(credenzialiUtente.validaCredenzialiUtente(email,password)){
            Utente utente=new Utente();
            utente.setIdUtente(credenzialiUtente.cercaIdUtente(email));
            SessioneCorrente.setUtenteCorrente(utente);
            return utente;
        }
        return null;
    }

    /**
     * Dato un utente, ritorna true se è il suo primo accesso, false altrimenti.
     * @param u Utente
     * @return valore booleano
     */
    public boolean isPrimoAcceso(Utente u) {
        return utenteDao.isPrimoAccesso(u.getIdUtente());
    }

    /**
     * Imposta il primo accesso dell'utente a False.
     * @param u Utente
     */
    public void impostaPrimoAccesso(Utente u) {
        utenteDao.impostaPrimoAccesso(u.getIdUtente());
    }

    /**
     * Controlla se l'email sia stata già utilizzata da qualche altro utente registrato.
     * @param Email Email dell'utente.
     * @return
     */
    public boolean controlloEmailEsistente(String Email){
        return credenzialiUtente.cercaEmail(Email);
    }


    /**
     * Data un email, ritorna la domanda di sicurezza ad essa associata.
     * @param email Email dell'utente.
     * @return Stringa contentente la domanda di sicurezza.
     */
    public String getDomandaSicurezzaUtente(String email){
        return credenzialiUtente.getDomandaSicurezza(email);
    }

    /**
     * data un'email e una risposta alla domanda di sicurezza, la funzione ha il compito di verificare che la risposta
     * sia corretta.
     * @param email Email dell'utente.
     * @param risposta Risposta alla domanda di sicurezza.
     * @return
     */
    public boolean controlloRispostaDomandaSicurezza(String email,String risposta){
        return credenzialiUtente.confrontoRispostaSicurezza(email,risposta);
    }

    /**
     * Cambia la password associata all'email selezionata.
     * @param email Email dell'utente.
     * @param password Nuova password da aggiornare.
     * @return
     */
    public boolean cambiaPassword(String email,String password){
        String pwcript=BCrypt.hashpw(password,BCrypt.gensalt(12));
        return credenzialiUtente.cambiaPassword(email,pwcript);
    }
}