package it.unical.serialmente.TechnicalServices.Persistence.model;

public class SessioneCorrente {
    private static Utente utenteCorrente;

    public SessioneCorrente() {}

    public static Utente getUtenteCorrente() {
        return utenteCorrente;
    }


    public static void setUtenteCorrente(Utente utenteCorrente) {
        SessioneCorrente.utenteCorrente = utenteCorrente;
    }

    public static void resetSessioneCorrente() {
        SessioneCorrente.utenteCorrente = null;
    }

}
