package universite_paris8.iut.lefarwestenperil.sae2_04.Modele;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Dragon;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Ennemi;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Gardien;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Link;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Cowboy;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.BouleDeFeu;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.Fleche;

import java.util.*;

/**
 * La classe Environnement représente le contexte global du jeu, incluant les ennemis, leur barre de vie et le terrain.
 */
public class Environnement {
    private ObservableList<Ennemi> ennemis;
    private ObservableList<BarreDeVie> barreDeVies;
    private ObservableList<Fleche> fleches;
    private ObservableList<BouleDeFeu> boulesDeFeu;
    private IntegerProperty nombreEnnemis;
    private Link link;
    private int tours;
    private Terrain terrain;
    private ObservableList<Gardien> gardiens;


    public Environnement(Terrain terrain, Link link) {
        this.ennemis = FXCollections.observableArrayList();
        this.barreDeVies = FXCollections.observableArrayList();
        this.fleches = FXCollections.observableArrayList();
        this.boulesDeFeu = FXCollections.observableArrayList();
        this.tours = 0;
        this.terrain = terrain;
        this.nombreEnnemis = new SimpleIntegerProperty(0);
        this.link = link;
        this.gardiens = FXCollections.observableArrayList();
    }

    public Terrain getTerrain(){
        return terrain;
    }

    public void ajouterGardien(Gardien gardien) {
        gardiens.add(gardien);
    }

    public ObservableList<Gardien> getGardiens() {
        return gardiens;
    }



    public void ajouterEnnemisAleatoirement(int nombreEnnemis) {
        Random rand = new Random();
        int largeurMap = terrain.getLargeur();
        int hauteurMap = terrain.getHauteur();

        int nombreCowboysHautGauche = (int) (nombreEnnemis * 0.25);
        int nombreDragonsHautGauche = (int) (nombreEnnemis * 0.10);

        int nombreCowboysHautDroit = (int) (nombreEnnemis * 0.15);
        int nombreDragonsHautDroit = (int) (nombreEnnemis * 0.15);

        int nombreCowboysBasDroit = (int) (nombreEnnemis * 0.10);
        int nombreDragonsBasDroit = (int) (nombreEnnemis * 0.25);

        placerEnnemis(rand, nombreCowboysHautGauche, "Cowboy", 0, hauteurMap / 2, 0, largeurMap / 2);
        placerEnnemis(rand, nombreDragonsHautGauche, "Dragon", 0, hauteurMap / 2, 0, largeurMap / 2);
        placerEnnemis(rand, nombreCowboysHautDroit, "Cowboy", 0, hauteurMap / 2, largeurMap / 2, largeurMap);
        placerEnnemis(rand, nombreDragonsHautDroit, "Dragon", 0, hauteurMap / 2, largeurMap / 2, largeurMap);
        placerEnnemis(rand, nombreCowboysBasDroit, "Cowboy", hauteurMap / 2, hauteurMap, largeurMap / 2, largeurMap);
        placerEnnemis(rand, nombreDragonsBasDroit, "Dragon", hauteurMap / 2, hauteurMap, largeurMap / 2, largeurMap);
    }

    private void placerEnnemis(Random rand, int nombreEnnemis, String typeEnnemi, int minY, int maxY, int minX, int maxX) {
        for (int i = 0; i < nombreEnnemis; i++) {
            int x, y, largeurImage, hauteurImage;
            Ennemi ennemi;
            if (typeEnnemi.equals("Cowboy")) {
                ennemi = new Cowboy(terrain, this);
            } else {
                ennemi = new Dragon(terrain, this);
            }
            largeurImage = ennemi.getLargeurImage();
            hauteurImage = ennemi.getHauteurImage();

            do {
                x = minX * 32 + rand.nextInt((maxX - minX) * 32);
                y = minY * 32 + rand.nextInt((maxY - minY) * 32);
            } while (!terrain.estMarchable(y / 32, x / 32) || !terrain.estMarchable((y + hauteurImage - 1) / 32, (x + largeurImage - 1) / 32));

            ennemi.setX(x);
            ennemi.setY(y);
            ennemis.add(ennemi);
            ajouterBarreDeVie(ennemi.getBarreDeVie());
        }
    }

    public void ajouterBarreDeVie(BarreDeVie b) {
        barreDeVies.add(b);
    }

    public void ajouterFleche(Fleche f) {
        fleches.add(f);
    }

    public void ajouterBouleDeFeu(BouleDeFeu bdf) {
        boulesDeFeu.add(bdf);
    }


    public void unTour() {
        for (int i = 0; i < ennemis.size(); i++) {
            Ennemi e = ennemis.get(i);
            e.seDeplacer(link);
            e.getBarreDeVie().setX(e.getX());
            e.getBarreDeVie().setY(e.getY());
            e.getBarreDeVie().setVieActuelle(e.getPointVie());
            e.getBarreDeVie().miseAJourVieTotale();
            if (!e.estVivant()) {
                ennemis.remove(i);
                i--;
            }
        }

        for (int i = 0; i < boulesDeFeu.size(); i++) {
            BouleDeFeu bdf = boulesDeFeu.get(i);
            if (bdf.isActive()) {
                bdf.deplacer();
                if (tours - bdf.getCreationTour() >= 50) {
                    bdf.explosion();
                }
            }else{
                boulesDeFeu.remove(bdf);
            }
        }

        if (link.isBrule()) {
            link.brulure();
        }

        this.tours++;
    }

    public ObservableList<BarreDeVie> getBarreDeVies() {
        return barreDeVies;
    }

    public ObservableList<Fleche> getFleches() {
        return fleches;
    }

    public ObservableList<BouleDeFeu> getBoulesDeFeu() {
        return boulesDeFeu;
    }

    public int getTours() {
        return tours;
    }

    public Link getLink() {
        return link;
    }

    public ObservableList<Ennemi> getEnnemis() {
        return ennemis;
    }

    public List<Ennemi> getEnnemisDansRayon(int x, int y, int rayon) {
        List<Ennemi> ennemisDansRayon = new ArrayList<>();
        for (Ennemi ennemi : ennemis) {
            double distance = Math.sqrt(Math.pow(ennemi.getX() - x, 2) + Math.pow(ennemi.getY() - y, 2));
            if (distance <= rayon) {
                ennemisDansRayon.add(ennemi);
            }
        }
        return ennemisDansRayon;
    }

    public void ajouterQuestionGardien(){
        List<String> choix = new ArrayList<>();
        choix.add("Oui");
        choix.add("Non");
        ajouterGardien(new Gardien(2944, 544, "Lexpression «Creuse où tu te tiens » signifie-t-elle qu'on peut trouver des opportunités ou des solutions autour de soi sans chercher ailleurs ?", choix, "Oui", "Bien joué ! Récupère ton marteau qui te permet de casser des cactus pour sauver ton papa qui est coincé dedans(en changeant d'arme).", 0, this));
        ajouterGardien(new Gardien(2144, 832, "L'expression indienne « Comme les rats quittent un navire qui coule » est-elle utilisée pour décrire les gens qui abandonnent une situation difficile ou désespérée ?", choix, "Oui", "Bonne réponse! Tu as 1 cœur de vie en plus !", 1, this));
        ajouterGardien(new Gardien(1600, 1000, "Est-ce que l'expression indienne « Un chameau ne passe pas par le chas d'une aiguille » signifie qu'il est possible pour une personne arrogante de se montrer humble ?", choix, "Non", "Bonne réponse! Tu as 1 cœur de vie en plus !",1, this));
    }
    public void ajouterBrulure() {
        link.setBrule();
    }


    public boolean verifierVictoire() {
            int x = link.getX();
            int y = link.getY();
            return getTerrain().getTab()[y/32][x/32] == 12 ;
        }

    public Gardien verifierRencontreLinkGardien() {
        for (Gardien gardien : gardiens) {
            if (link.getX()/32 == gardien.getX()/32 && link.getY()/32 == gardien.getY()/32) {
                return gardien;
            }
        }
        return null;
    }
}

