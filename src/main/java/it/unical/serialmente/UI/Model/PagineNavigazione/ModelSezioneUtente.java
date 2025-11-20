package it.unical.serialmente.UI.Model.PagineNavigazione;

import it.unical.serialmente.Application.Service.SezioneUtenteService;
import it.unical.serialmente.Application.Service.UtenteService;
import it.unical.serialmente.Domain.model.Titolo;

import java.sql.SQLException;
import java.util.List;

public class ModelSezioneUtente {
    public record StatisticheSerieTv(List<Integer> durate,Integer episodiVisti){}
    private final SezioneUtenteService sezioneUtenteService = new SezioneUtenteService();
    private final UtenteService utenteService = new UtenteService();


    public String getNomeUtente(){
        return utenteService.getNomeUtente();
    }
    public List<Titolo> getTitoliInLista(String tipoLista,String tipoTitolo) throws SQLException {
        return sezioneUtenteService.getTitoliInLista(tipoLista,tipoTitolo);
    }
    public Integer getNmeroFilmVisionati(){
        return sezioneUtenteService.getNumeroFilmVisionati();
    }

    public List<Integer> getOreGiorniMesiVisionatiFilm(){
        return sezioneUtenteService.getOreGiorniMesiVisionatiFilm();
    }

    public StatisticheSerieTv getStatisticheSerieTv() throws Exception {
        return sezioneUtenteService.getStatisticheSerieTv();
    }

    public void rendiTitoloPreferito(Titolo titolo) throws Exception {
        sezioneUtenteService.rendiTitoloPreferito(titolo);
    }

    public void togliTitoloDaiPreferiti(Titolo titolo) throws Exception {
        sezioneUtenteService.togliTitoloPreferito(titolo);
    }
    public void rimuoviTitoloDaVisionati(Titolo titolo) throws Exception {
        sezioneUtenteService.rimuoviTitoloDaVisionati(titolo);
    }
}
