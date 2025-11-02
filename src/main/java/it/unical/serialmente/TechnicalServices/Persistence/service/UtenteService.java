package it.unical.serialmente.TechnicalServices.Persistence.service;

import it.unical.serialmente.TechnicalServices.Persistence.dao.CredenzialiUtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.dao.UtenteDAO;
import it.unical.serialmente.TechnicalServices.Persistence.model.CredenzialiUtente;
import it.unical.serialmente.TechnicalServices.Persistence.model.Utente;

public class UtenteService {
    private UtenteDAO utenteDao;
    private CredenzialiUtenteDAO credenzialiUtente;

    public UtenteService(UtenteDAO utenteDao, CredenzialiUtenteDAO utente) {
        this.utenteDao=utenteDao;
        this.credenzialiUtente=utente;

    }

    /**
     * Registra l'utente all'interno dell'applicazione, prendendo tutte le informazioni necessarie all'interno
     * dell'oggetto Utente.
     * @param utente
     * @return valore booleano per confermare o meno l'operazione.
     */
    public boolean registraUtente(Utente utente){
        CredenzialiUtente credenziali=utente.getCredenzialiUtente();
        if(credenzialiUtente.cercaEmail(credenziali.getEmail())){
            System.out.println("Email già presente");
            return false;
        }
        if(utenteDao.inserisciUtente(utente)){
            if(credenzialiUtente.insertCredenzialiUtente(credenziali,utente.getIdUtente())){
                System.out.println("Registrazione effettuata");
            }
            else {
                System.out.println("Registrazione non effettuata");
                utenteDao.cancellaUtente(utente.getIdUtente());
                return false;
            }
        }
        else {
            return false;
        }
        return true;
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
            CredenzialiUtente credenziali= credenzialiUtente.getCredenzialiUtente(utente);
            utente.setCredenzialiUtente(credenziali);
            System.out.println("Login effettuata");
            return utente;
        }
        System.out.println("CredenzialiUtente non validata");
        return null;
    }


}
