package universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Armes.Arme;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Armes.Bombe;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Environnement;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Terrain;

import java.util.ArrayList;
import java.util.List;

public class Personnage {
    protected int tailleTuile = 32;
    private int pointVie;
    private int pointAttaque;
    private int pointDefense;
    private Environnement env;
    private int vitesseDeplacement;
    private List<Arme> armes;
    private Arme armeActuelle;
    private int indexArmeActuelle;
    private IntegerProperty x, y;

    private Terrain terrain;
    private boolean brule;
    protected int direction;
    private static int compteurBrulure;
    private int pointVieMax;



    public Personnage(int x, int y,int pointVie, int pointAttaque, int pointDefense,Terrain terrain) {
        this.pointVie = pointVie;
        this.pointVieMax = pointVie;
        this.pointAttaque = pointAttaque;
        this.pointDefense = pointDefense;
        this.armes = new ArrayList<>();
        this.armeActuelle = null;
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.terrain = terrain;
        this.vitesseDeplacement=4;
        this.indexArmeActuelle = 0;
        this.brule = false;
        this.compteurBrulure = 0;
    }

    public Personnage(int x, int y, Terrain terrain) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.terrain = terrain;
    }

    public boolean isBrule(){
        return this.brule;
    }



    public void brulure(){
        if(compteurBrulure % 10 == 0){
            this.setPointVie(getPointVie()-1);
            if (compteurBrulure==30){
                compteurBrulure = 0;
                brule= false;
            }
        }
        compteurBrulure++;
    }

    public void ajouterArme(Arme arme) {
        this.armes.add(arme);
        if (this.armes.size() == 1) {
            this.armeActuelle = arme;
        }
    }
    public Arme getArme(){
        return armeActuelle;
    }

    public int getPointVieMax() {
        return pointVieMax;
    }

    public void setPointVieMax(int pointVieMax){
        this.pointVieMax = pointVieMax;
    }

    public void changerArmeSuivante() {
        if (!armes.isEmpty()) {
            indexArmeActuelle = (indexArmeActuelle + 1) % armes.size();
            armeActuelle = armes.get(indexArmeActuelle);
        }
        System.out.println("Armes: " + armes);
        System.out.println("Arme actuelle après changement suivant: " + getArmeActuelle());
    }

    public void changerArmePrecedente() {
        if (!armes.isEmpty()) {
            indexArmeActuelle = (indexArmeActuelle - 1 + armes.size()) % armes.size();
            armeActuelle = armes.get(indexArmeActuelle);
        }
        System.out.println("Armes: " + armes);
        System.out.println("Arme actuelle après changement précédent: " + armeActuelle);
    }



    public int getDirection() {
        return direction;
    }

    public void attaque(List<Ennemi> cibles) {
        if (armeActuelle != null) {
            if (getArme() instanceof Bombe) {
                Bombe bombe = (Bombe) getArme();
                System.out.println("bombe");
                if (bombe.estEnCours()) {
                    //System.out.println("Une bombe est déjà en cours. Veuillez attendre l'explosion.");
                    return;
                }
            }
            armeActuelle.attaquer(this, cibles);
        } else {
            System.out.println("Aucune arme pour l'attaque directionnelle.");
        }
    }

    public void setPointVie(int pointVie) {
        this.pointVie = pointVie;
    }

    public boolean estVivant() {
        return this.pointVie > 0;
    }

    public IntegerProperty vieProperty(){
        return new SimpleIntegerProperty(pointVie);
    }

    public int getTailleTuile() {
        return tailleTuile;
    }

    public int getPointVie() {
        return pointVie;
    }

    public int getPointAttaque() {
        return pointAttaque;
    }

    public int getPointDefense() {
        return pointDefense;
    }

    public Environnement getEnv() {
        return env;
    }

    public int getVitesseDeplacement() {
        return vitesseDeplacement;
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public IntegerProperty yProperty() {
        return y;
    }



    public final int getX() {
        return this.xProperty().getValue();
    }

    public final void setX(int n) {
        this.xProperty().setValue(n);
    }

    public final int getY() {
        return this.yProperty().getValue();
    }

    public final void setY(int n) {
        this.yProperty().setValue(n);
    }

    public Terrain getTerrain() {
        return terrain;
    }


    public void recevoirDegats(int pointsDegats) {
        int degatReel = pointsDegats - this.pointDefense;
        System.out.println("degat reel :" + degatReel);
        if (degatReel > 0) {
            if (this.pointVie >= degatReel)
                this.pointVie -= degatReel;
            else {
                this.pointVie = 0;
            }
        }
        System.out.println(pointVie);
    }

    public Arme getArmeActuelle() {
        if(this.armes.size() == 1) {
            this.armeActuelle = this.armes.get(armes.size()-1);
        }
        if (this.armes.size()>=2) {
            armeActuelle = this.armes.get(armes.size()-1);
        }
        return armeActuelle;
    }
    public List<Arme> getArmes() {
        return this.armes;
    }

    public void ramasserArme(Arme arme) {
        this.armes.add(arme);
        System.out.println(armes);
    }



    public void setArmeActuelle(Arme armeActuelle) {
        this.armeActuelle = armeActuelle;
    }


    @Override
    public String toString() {
        return "Personnage{" +
                "pointVie=" + pointVie +
                ", pointAttaque=" + pointAttaque +
                ", pointDefense=" + pointDefense +
                ", armes=" + armes +
                '}';
    }

    public void setBrule() {
        this.brule = true;
    }
}