package universite_paris8.iut.lefarwestenperil.sae2_04.Modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.*;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.BouleDeFeu;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.Fleche;

import java.util.ArrayList;
import java.util.List;


/**
 * La classe Environnement représente le contexte global du jeu, incluant les ennemis, les gardiens, les projectiles, etc.
 */

public class Environnement {
    private GestionEnnemi gestionEnnemi;
    private GestionProjectile gestionProjectile;

    private ObservableList<BarreDeVie> barreDeVies;
    private ObservableList<Gardien> gardiens;
    private Link link;

    private Terrain terrain;
    private int tours;

    public Environnement(Terrain terrain, Link link) {
        this.terrain = terrain;
        this.link = link;
        this.gestionEnnemi = new GestionEnnemi(terrain, this);
        this.gestionProjectile = new GestionProjectile();
        this.barreDeVies = FXCollections.observableArrayList();
        this.gardiens = FXCollections.observableArrayList();
        this.tours = 0;
    }


    public void ajouterEnnemisAleatoirement(int nombreEnnemis) {
        gestionEnnemi.ajouterEnnemisAleatoirement(nombreEnnemis);
    }

    public void ajouterGardien(Gardien gardien) {
        gardiens.add(gardien);
    }

    public void ajouterQuestionGardien(){
        List<String> choix = new ArrayList<>();
        choix.add("Oui");
        choix.add("Non");
        ajouterGardien(new Gardien(2944, 544, "Lexpression «Creuse où tu te tiens » signifie-t-elle qu'on peut trouver des opportunités ou des solutions autour de soi sans chercher ailleurs ?", choix, "Oui", "Bien joué ! Récupère ton marteau qui te permet de casser des cactus pour sauver ton papa qui est coincé dedans(en changeant d'arme).", 0, this));
        ajouterGardien(new Gardien(2144, 832, "L'expression indienne « Comme les rats quittent un navire qui coule » est-elle utilisée pour décrire les gens qui abandonnent une situation difficile ou désespérée ?", choix, "Oui", "Bonne réponse! Tu as 1 cœur de vie en plus !", 1, this));
        ajouterGardien(new Gardien(1600, 1000, "Est-ce que l'expression indienne « Un chameau ne passe pas par le chas d'une aiguille » signifie qu'il est possible pour une personne arrogante de se montrer humble ?", choix, "Non", "Bonne réponse! Tu as 1 cœur de vie en plus !",1, this));
    }


    public void ajouterBarreDeVie(BarreDeVie b) {
        barreDeVies.add(b);
    }

    public void ajouterFleche(Fleche f) {
        gestionProjectile.ajouterProjectile(f);
    }

    public void ajouterBouleDeFeu(BouleDeFeu bdf) {
        gestionProjectile.ajouterProjectile(bdf);
    }

    public void unTour() {
        gestionEnnemi.deplacerEnnemis(link);
        gestionEnnemi.miseAjour();
        gestionProjectile.mettreAJourProjectiles(tours);
        if (link.isBrule()) {
            link.brulure();
        }
        this.tours++;
    }

    public Gardien verifierRencontreLinkGardien() {
        // Vérifie si Link rencontre un Gardien

        for (Gardien gardien : gardiens) {
            if (link.getX() / 32 == gardien.getX() / 32 && link.getY() / 32 == gardien.getY() / 32) {
                return gardien;
            }
        }
        return null;
    }

    public boolean verifierVictoire() {
        // Vérifie la victoire de Link

        int x = link.getX();
        int y = link.getY();
        return terrain.getTab()[y / 32][x / 32] == 12;
    }
    public void ajouterBrulure() {
        link.setBrule();
    }


    public Terrain getTerrain() {
        return terrain;
    }

    public ObservableList<Ennemi> getEnnemis() {
        return gestionEnnemi.getEnnemis();
    }

    public ObservableList<BarreDeVie> getBarreDeVies() {
        return barreDeVies;
    }

    public ObservableList<Gardien> getGardiens() {
        return gardiens;
    }

    public ObservableList<Fleche> getFleches() {
        return gestionProjectile.getFleches();
    }

    public ObservableList<BouleDeFeu> getBoulesDeFeu() {
        return gestionProjectile.getBoulesDeFeu();
    }

    public Link getLink() {
        return link;
    }

    public int getTours() {
        return tours;
    }

}