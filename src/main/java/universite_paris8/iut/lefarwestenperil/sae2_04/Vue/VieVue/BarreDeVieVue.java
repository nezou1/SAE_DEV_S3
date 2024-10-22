package universite_paris8.iut.lefarwestenperil.sae2_04.Vue.VieVue;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.BarreDeVie;

/**
 * La classe BarreDeVieVue est responsable de l'affichage graphique des barres de vie dans le panneau de jeu.
 */
public class BarreDeVieVue {
    // Panneau de jeu où les barres de vie seront affichées
    private Pane panneauJeu;

    /**
     * Constructeur pour initialiser BarreDeVieVue avec le panneau de jeu donné.
     * @param panneauJeu le panneau de jeu où les barres de vie seront affichées
     */
    public BarreDeVieVue(Pane panneauJeu) {
        this.panneauJeu = panneauJeu;
    }

    /**
     * Affiche une barre de vie sur le panneau de jeu.
     * @param barre l'objet BarreDeVie à afficher
     */
    public void afficherBarreVie(BarreDeVie barre) {
        // Crée une nouvelle ProgressBar pour représenter la barre de vie
        ProgressBar barreDeVie = new ProgressBar();
        barreDeVie.setId(barre.getId());
        barreDeVie.setProgress(barre.getVieRestante()); // Ajuste la valeur de progression de la barre de vie
        barreDeVie.setTranslateX(barre.getX());
        barreDeVie.setTranslateY(barre.getY());
        barreDeVie.setMaxHeight(10);
        barreDeVie.setMaxWidth(30);
        barreDeVie.setStyle("-fx-accent: green"); // Utilise la couleur appropriée en fonction de la vie
        this.panneauJeu.getChildren().add(barreDeVie);
        System.out.println(barre.getVieRestante());

        // Lie les propriétés de position et de vie de la barre de vie à celles de l'objet BarreDeVie
        barreDeVie.translateXProperty().bind(barre.xProperty());
        barreDeVie.translateYProperty().bind(barre.yProperty());
        barreDeVie.progressProperty().bind(barre.vieRestanteProperty());

        // Ajoute un listener pour changer la couleur de la barre de vie en fonction du pourcentage de vie
        barre.vieRestanteProperty().addListener(event -> {
            double pourcentageVie = barre.getVieRestante();
            if (pourcentageVie <= 0.55) {
                barreDeVie.setStyle("-fx-accent: red"); // Rouge pour moins de 55% de vie
            } else if (pourcentageVie <= 0.85) {
                barreDeVie.setStyle("-fx-accent: orange"); // Orange pour 55% à 85% de vie
            } else {
                barreDeVie.setStyle("-fx-accent: green"); // Vert pour plus de 85% de vie
            }
        });
    }
}
