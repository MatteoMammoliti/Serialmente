package it.unical.serialmente.Application.Service;

import it.unical.serialmente.TechnicalServices.Persistence.DBManager;
import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;
import it.unical.serialmente.Domain.model.SessioneCorrente;
import it.unical.serialmente.Domain.model.Utente;

import java.sql.SQLException;

public class UtenteService {
    private UtenteDAO utenteDao = DBManager.getInstance().getUtenteDAO();
    private CredenzialiUtenteDAO credenzialiUtente = DBManager.getInstance().getCredenzialiUtenteDAO();

    /**
     * Registra l'utente all'interno dell'applicazione, prendendo tutte le informazioni necessarie all'interno
     * dell'oggetto Utente.
     * @param "nome,email,password,domanda sicurezza e risposta domnda sicurezza"
     * @return valore booleano per confermare o meno l'operazione.
     */
    public boolean registraUtente(String nome,String email,String password,String domandaSicurezza,String rispostaDomandaSicurezza) throws SQLException {
        try{
            DBManager.getInstance().getConnection().setAutoCommit(false);
            boolean emailtrovata;
            boolean inserimentoRiuscito;
            emailtrovata= credenzialiUtente.cercaEmail(email);
            if(emailtrovata){
                System.out.println("Email già presente");
                DBManager.getInstance().getConnection().rollback();
            }
            int inserisciUtente= utenteDao.inserisciUtente(nome);
            if(inserisciUtente==0){
                DBManager.getInstance().getConnection().rollback();
            }
            inserimentoRiuscito=credenzialiUtente.insertCredenzialiUtente(email,password,domandaSicurezza,rispostaDomandaSicurezza,inserisciUtente);
            if(!inserimentoRiuscito){
                DBManager.getInstance().getConnection().rollback();
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
     * @param email
     * @param password
     * @return L'oggetto dell'Utente in questione, in caso di insucesso NULL.
     */
    public Utente loginUtente(String email, String password){
        if(credenzialiUtente.validaCredenzialiUtente(email,password)){
            Utente utente=new Utente();
            utente.setIdUtente(credenzialiUtente.cercaIdUtente(email));
            SessioneCorrente.setUtenteCorrente(utente);

            System.out.println("Login effettuata");
            return utente;
        }
        System.out.println("CredenzialiUtente non validata");
        return null;
    }

    public boolean isPrimoAcceso(Utente u) {
        return utenteDao.isPrimoAccesso(u.getIdUtente());
    }

    public void impostaPrimoAccesso(Utente u) {
        utenteDao.impostaPrimoAccesso(u.getIdUtente());
    }
}