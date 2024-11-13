package universite_paris8.iut.lefarwestenperil.sae2_04.Controleur;


import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import universite_paris8.iut.lefarwestenperil.sae2_04.MyPackage.Entites.EtreVivants.Ennemi2;
import universite_paris8.iut.lefarwestenperil.sae2_04.Vue.PersonnageVue.EnnemiVue;


public class ListObsEnnemis implements ListChangeListener<Ennemi2> {

    private EnnemiVue ennemisVue;

    public ListObsEnnemis(Pane PanneauJeu) {
        ennemisVue = new EnnemiVue(PanneauJeu);
    }

    @Override
    public void onChanged(Change<? extends Ennemi2> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                for (Ennemi2 e : change.getAddedSubList()) {
//                    System.out.println("add");
                    ennemisVue.creerEnnemi(e);
                }
            }
            if (change.wasRemoved()) {
                for (Ennemi2 e : change.getRemoved()) {
//                    System.out.println("supp");
                    ennemisVue.supprimerEnnemi(e);
                }
            }
        }
    }
}