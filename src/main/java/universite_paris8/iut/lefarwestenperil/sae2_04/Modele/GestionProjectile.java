package universite_paris8.iut.lefarwestenperil.sae2_04.Modele;

import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.BouleDeFeu;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.Fleche;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Ici on se concentre sur la gestion des projectils, qui comprenait l'ajout, le déplacement et la detection de link.
 * Tout cela se trouvait dans la class Environnement et a été déplacée dans une nouvelle classe appelée GestionEnnemi.
 */

public class GestionProjectile {
    private ObservableList<Fleche> fleches;
    private ObservableList<BouleDeFeu> boulesDeFeu;

    public GestionProjectile() {
        this.fleches = FXCollections.observableArrayList();
        this.boulesDeFeu = FXCollections.observableArrayList();
    }

    public void ajouterProjectile(Fleche fleche) {
        fleches.add(fleche);
    }

    public void ajouterProjectile(BouleDeFeu bouleDeFeu) {
        boulesDeFeu.add(bouleDeFeu);
    }

    public void mettreAJourProjectiles(int tours) {
        for (BouleDeFeu bdf : boulesDeFeu) {
            if (bdf.isActive()) {
                bdf.deplacer();
                if (tours - bdf.getCreationTour() >= 50) {
                    bdf.explosion();
                }
            } else {
                boulesDeFeu.remove(bdf);
            }
        }
    }

    public ObservableList<Fleche> getFleches() {
        return fleches;
    }

    public ObservableList<BouleDeFeu> getBoulesDeFeu() {
        return boulesDeFeu;
    }
}