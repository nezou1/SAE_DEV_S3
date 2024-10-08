package universite_paris8.iut.lefarwestenperil.sae2_04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.BouleDeFeu;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Environnement;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Link;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Terrain;

import static org.junit.jupiter.api.Assertions.*;

public class BouleDeFeuTest {
    private Environnement env;
    private Link link;
    private BouleDeFeu bouleDeFeu;

    @BeforeEach
    public void setUp() {
        Terrain terrain = new Terrain();
        link = new Link(terrain);
        env = new Environnement(terrain, link);
        bouleDeFeu = new BouleDeFeu(0, 0, link.getX(), link.getY(), 10, env);
    }

    @Test
    public void testBouleDeFeuInitialisation() {
        assertEquals(0, bouleDeFeu.getX());
        assertEquals(0, bouleDeFeu.getY());
        assertEquals(env.getTours(), bouleDeFeu.getCreationTour());
        assertTrue(bouleDeFeu.isActive());
    }

    @Test
    public void testDeplacerVersCible() {
        link.setX(100);
        link.setY(100);
        bouleDeFeu.deplacer();
        assertTrue(bouleDeFeu.getX() > 0);
        assertTrue(bouleDeFeu.getY() > 0);
    }

    @Test
    public void testExplosion() {
        bouleDeFeu.explosion();
        assertFalse(bouleDeFeu.isActive());
    }

    @Test
    public void testInfligerDegats() {
        int initialPV = link.getPointVie();
        bouleDeFeu.infligerDegats(link);
        assertEquals(initialPV - 10, link.getPointVie());
    }

    @Test
    public void testDeplacerEtExplosion() {
        link.setX(4);
        link.setY(4);
        bouleDeFeu.deplacer();
        assertFalse(bouleDeFeu.isActive());
        assertTrue(link.getPointVie() < 1600); // assuming initial HP of Link is 1600
    }
}
