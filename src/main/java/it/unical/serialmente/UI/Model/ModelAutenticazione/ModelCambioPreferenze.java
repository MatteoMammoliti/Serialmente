package it.unical.serialmente.UI.Model.ModelAutenticazione;

import it.unical.serialmente.Application.Service.PreferenzeService;
import it.unical.serialmente.Domain.model.Genere;
import it.unical.serialmente.Domain.model.Piattaforma;
import java.util.HashSet;
import java.util.List;


public class ModelCambioPreferenze {

    PreferenzeService preferenzeService = new  PreferenzeService();
    public HashSet<Integer> idGeneri = new HashSet<>();
    private final HashSet<Integer> idPiattaforme=new HashSet<>();

    public List<Genere> getGeneriPreferiti(){return preferenzeService.visualizzaPreferenzeGenereUtente();}
    public List<Piattaforma> getPiattaformePreferite() throws Exception {return preferenzeService.visualizzaPreferenzePiattaformeUtente();}
    public List<Genere>getTuttiGeneri(){
        List<Genere>serie= preferenzeService.getGeneriPerSerieTV();
        List<Genere> film = preferenzeService.getGeneriPerFilm();
        serie.addAll(film);
        return serie;
    }
    public List<Piattaforma> getTuttePiattafome(){return preferenzeService.getPiattaformeDisponibili();};

    public void addGenere(Integer id){idGeneri.add(id);}
    public void rimuoviGenere(Integer id){idGeneri.remove(id);}

    public void riempiGeneriPrecedenti(List<Genere> generi){
        for(Genere g: generi){
            idGeneri.add(g.getIdGenere());
        }
    }
    public void riempiPiattformePrecedenti(List<Piattaforma> piattaforme){
        for(Piattaforma p: piattaforme){
            idPiattaforme.add(p.getIdPiattaforma());
        }
    }
    public void addPiattaforma(Integer id){idPiattaforme.add(id);}
    public void rimuoviPiattaforma(Integer id){idPiattaforme.remove(id);}
    public void applicaModifiche(List<Genere> generiPrecedenti,List<Piattaforma> piattaformePrecedenti) throws Exception {
        HashSet<Integer> generiPrecedentiId=new HashSet<>();
        for(Genere g: generiPrecedenti){
            generiPrecedentiId.add(g.getIdGenere());
        }

        HashSet<Integer> piattaformePrecedentiId=new HashSet<>();
        for(Piattaforma p: piattaformePrecedenti){
            piattaformePrecedentiId.add(p.getIdPiattaforma());
        }


        HashSet<Integer> generiDaAggiungere=new HashSet<>(idGeneri);
        generiDaAggiungere.removeAll(generiPrecedentiId);
        HashSet<Integer> generiDaEliminare = new HashSet<>(generiPrecedentiId);
        generiDaEliminare.removeAll(idGeneri);

        HashSet<Integer> piattaformeDaAggiungere=new HashSet<>(idPiattaforme);
        piattaformeDaAggiungere.removeAll(piattaformePrecedentiId);

        HashSet<Integer> piattaformeDaEliminare = new HashSet<>(piattaformePrecedentiId);
        piattaformeDaEliminare.removeAll(idPiattaforme);

        preferenzeService.applicaModificaPreferenze(generiDaEliminare,piattaformeDaEliminare,generiDaAggiungere,piattaformeDaAggiungere);

    }
}
