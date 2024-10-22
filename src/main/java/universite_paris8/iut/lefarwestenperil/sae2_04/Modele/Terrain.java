package universite_paris8.iut.lefarwestenperil.sae2_04.Modele;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialise le terrain en chargeant les données depuis le fichier choisis.
 */

public class Terrain {

    private final int tailleTuile = 32;

    private int [][] donneeTerrain;

    public Terrain() {
        chargerTerrain();
    }

    private void chargerTerrain() {
        List<int[]> lignes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/universite_paris8/iut/lefarwestenperil/sae2_04/terrain.txt"))) {
            String ligne;
            ligne = reader.readLine();
            while (ligne != null) {
                String[] valeurs = ligne.split(",");
                int[] ligneTab = new int[valeurs.length];
                for (int i = 0; i < valeurs.length; i++) {
                    ligneTab[i] = Integer.parseInt(valeurs[i].trim());
                }
                lignes.add(ligneTab);
                ligne= reader.readLine();
            }
            donneeTerrain = lignes.toArray(new int[0][]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(donneeTerrain.length+"  "+ donneeTerrain[0].length);
    }


    public int[][] getTab() {
        return this.donneeTerrain;
    }

    public int getHauteur() {
        return donneeTerrain.length;
    }

    public int getLargeur() {
        return donneeTerrain[0].length;
    }

    public boolean estMarchable(int y, int x) {
        int tileX = x / tailleTuile;
        int tileY = y / tailleTuile;
        if (tileX >= 0 && tileX < getLargeur() && tileY >= 0 && tileY < getHauteur()) {
            return donneeTerrain[tileY][tileX] == 0 || donneeTerrain[tileY][tileX] == 12;
        }
        return false;
    }

}