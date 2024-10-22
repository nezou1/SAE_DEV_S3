package universite_paris8.iut.lefarwestenperil.sae2_04;


import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.*;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Dragon;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Ennemi;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Gardien;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Link;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Cowboy;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.BouleDeFeu;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnvironnementTest  {
    private Environnement environnement;
    private Terrain terrain;
    private Link link;
    private GestionEnnemi gestionEnnemi;
    private GestionProjectile gestionProjectile;

    @BeforeEach
    void setUp() {
        terrain = new Terrain(); // Supposons que le terrain est 20x20
        link = new Link(terrain);
        environnement = new Environnement(terrain, link);
        gestionEnnemi = new GestionEnnemi(terrain,environnement);
        gestionProjectile = new GestionProjectile();
    }

    @Test
    void testAjouterGardien() {
        Gardien gardien = new Gardien(100, 100, "Question", new ArrayList<>(), "Réponse", "Message", 0, environnement);
        environnement.ajouterGardien(gardien);
        ObservableList<Gardien> gardiens = environnement.getGardiens();
        assertEquals(1, gardiens.size());
        assertEquals(gardien, gardiens.get(0));
    }



    @Test
    void testUnTour() {
        Cowboy cowboy = new Cowboy(terrain, environnement);
        environnement.getEnnemis().add(cowboy);
        environnement.unTour();
        assertEquals(1, environnement.getTours());
    }



    @Test
    void testAjouterBouleDeFeu() {
        BouleDeFeu bouleDeFeu = new BouleDeFeu(1,2,3,4,1,environnement);
        environnement.ajouterBouleDeFeu(bouleDeFeu);
        ObservableList<BouleDeFeu> boulesDeFeu = environnement.getBoulesDeFeu();
        assertEquals(1, boulesDeFeu.size());
        assertEquals(bouleDeFeu, boulesDeFeu.get(0));
    }

    @Test
    void testGetEnnemisDansRayon() {
        Ennemi ennemi1 = new Cowboy(terrain, environnement);
        ennemi1.setX(10);
        ennemi1.setY(10);
        Ennemi ennemi2 = new Dragon(terrain, environnement);
        ennemi2.setX(100);
        ennemi2.setY(100);
        environnement.getEnnemis().addAll(ennemi1, ennemi2);
        List<Ennemi> ennemisDansRayon = gestionEnnemi.getEnnemisDansRayon(10, 10, 20);
        assertEquals(1, ennemisDansRayon.size());
        assertEquals(ennemi1, ennemisDansRayon.get(0));
    }

    @Test
    void testAjouterQuestionGardien() {
        environnement.ajouterQuestionGardien();
        ObservableList<Gardien> gardiens = environnement.getGardiens();
        assertEquals(3, gardiens.size());
    }

    @Test
    void testAjouterBrulure() {
        environnement.ajouterBrulure();
        assertTrue(link.isBrule());
    }

    @Test
    void testVerifierVictoire() {
        link.setX(32);
        link.setY(32);
        terrain.getTab()[1][1] = 12;
        assertTrue(environnement.verifierVictoire());
    }
}
