package it.unical.serialmente.TechnicalServices.Persistence.dao;

public interface CredenzialiUtenteDAO {

    boolean insertCredenzialiUtente(String email,
                                    String password,
                                    String domandaSicurezza,
                                    String rispostaDomandaSicurezza,
                                    Integer idUtente);

    boolean cercaEmail(String email);
    boolean validaCredenzialiUtente(String email, String password);
    Integer cercaIdUtente(String email);
    String getDomandaSicurezza(String email);
    boolean confrontoRispostaSicurezza(String email,String risposta);
    boolean cambiaPassword(String email,String password);
}