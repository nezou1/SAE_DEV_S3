package universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Environnement;

import java.util.ArrayList;
import java.util.List;

public class GestionGardien {
    private ObservableList<Gardien> gardiens;
    public GestionGardien(Environnement env) {
        this.gardiens= FXCollections.observableArrayList();
    }
    public void ajouterGardien(Gardien gardien) {
        gardiens.add(gardien);
    }

    public void ajouterQuestionGardien(){
        List<String> choix = new ArrayList<>();
        choix.add("Oui");
        choix.add("Non");
        ajouterGardien(new Gardien(2944, 544, "Lexpression «Creuse où tu te tiens » signifie-t-elle qu'on peut trouver des opportunités ou des solutions autour de soi sans chercher ailleurs ?", choix, "Oui", "Bien joué ! Récupère ton marteau qui te permet de casser des cactus pour sauver ton papa qui est coincé dedans(en changeant d'arme).", 0, null));
        ajouterGardien(new Gardien(2144, 832, "L'expression indienne « Comme les rats quittent un navire qui coule » est-elle utilisée pour décrire les gens qui abandonnent une situation difficile ou désespérée ?", choix, "Oui", "Bonne réponse! Tu as 1 cœur de vie en plus !", 1, null));
        ajouterGardien(new Gardien(1600, 1000, "Est-ce que l'expression indienne « Un chameau ne passe pas par le chas d'une aiguille » signifie qu'il est possible pour une personne arrogante de se montrer humble ?", choix, "Non", "Bonne réponse! Tu as 1 cœur de vie en plus !",1, null));
    }
    public Gardien verifierRencontreLinkGardien(Link link) {
        for (Gardien gardien : gardiens) {
            if (link.getX()/32 == gardien.getX()/32 && link.getY()/32 == gardien.getY()/32) {
                return gardien;
            }
        }
        return null;
    }
}