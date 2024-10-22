package universite_paris8.iut.lefarwestenperil.sae2_04.Controleur;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import universite_paris8.iut.lefarwestenperil.sae2_04.Main;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.*;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Armes.*;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Ennemi;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Gardien;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Personnage.Link;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.BouleDeFeu;
import universite_paris8.iut.lefarwestenperil.sae2_04.Modele.Projectiles.Fleche;
import universite_paris8.iut.lefarwestenperil.sae2_04.Vue.*;
import universite_paris8.iut.lefarwestenperil.sae2_04.Vue.ArmesVue.BombeVue;
import universite_paris8.iut.lefarwestenperil.sae2_04.Vue.PersonnageVue.LinkVue;
import universite_paris8.iut.lefarwestenperil.sae2_04.Vue.VieVue.ListCoeurVue;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class Controleur implements Initializable {
    private Terrain terrain;
    private Timeline gameLoop;
    private Environnement env;
    private Link link;
    private LinkVue linkVue;
    @FXML
    private Pane panneauDeJeu;
    @FXML
    private TilePane tuile;
    private Scale scaleTransform;
    @FXML
    private HBox vieBox;
    @FXML
    private Button pauseButton;
    private Parent root;
    private Stage stage;
    private ListCoeurVue coeurVue;
    private MessageVue messageVue;

    private Bombe bombe;
    private TerrainVue tv;
    private GestionEnnemi gestionEnnemi;
    private GestionProjectile gestionProjectile;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        terrain = new Terrain();
        link = new Link(terrain);

        env = new Environnement(terrain, link);
        gestionEnnemi = new GestionEnnemi(terrain, env);
        gestionProjectile = new GestionProjectile();


        tv = new TerrainVue(terrain, tuile);
        linkVue = new LinkVue(panneauDeJeu);
        messageVue = new MessageVue();

        tv.creerCarte();
        linkVue.creerLink(link);

        BombeVue bombeVue = new BombeVue(panneauDeJeu);
        bombe = new Bombe(panneauDeJeu, bombeVue);

        link.ramasserArme(new Tomahawk());
        link.ramasserArme(new TireALArc(env));

        ListChangeListener<Ennemi> listenE = new ListObsEnnemis(panneauDeJeu);
        env.getEnnemis().addListener(listenE);

        ListChangeListener<BarreDeVie> listenB = new ListObsBarreDeVie(panneauDeJeu);
        env.getBarreDeVies().addListener(listenB);

        ListChangeListener<Fleche> listenF = new ListObsFleche(panneauDeJeu);
        env.getFleches().addListener(listenF);

        ListChangeListener<BouleDeFeu> listenBF = new ListObsFeu(panneauDeJeu);
        env.getBoulesDeFeu().addListener(listenBF);

        env.ajouterQuestionGardien();

        this.env.ajouterEnnemisAleatoirement(50);
        panneauDeJeu.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::gererTouchePressee);
            }
        });


        scaleTransform = new Scale();
        panneauDeJeu.getTransforms().add(scaleTransform);


        scaleTransform.setPivotX(link.getX()-200);
        scaleTransform.setPivotY(link.getY()-200);

        coeurVue = new ListCoeurVue(link, vieBox);


        initAnimation();
        gameLoop.play();
    }

    private void setScale(double scale) {
        scaleTransform.setX(scale);
        scaleTransform.setY(scale);
    }

    public Pane getPanneauDeJeu() {
        return panneauDeJeu;
    }


    @FXML
    private void gererTouchePressee(KeyEvent event) {
        System.out.println("Touche pressée: " + event.getCode());
        switch (event.getCode()) {
            case Z:
                link.deplacerHaut();
                linkVue.updateImage("HAUT", link.getArme());
                break;
            case Q:
                link.deplacerGauche();
                linkVue.updateImage("GAUCHE", link.getArme());
                break;
            case S:
                link.deplacerBas();
                linkVue.updateImage("BAS", link.getArme());
                break;
            case D:
                link.deplacerDroite();
                linkVue.updateImage("DROITE", link.getArme());
                break;
            case I:
                if(link.getArme() != null) {
                    List<Ennemi> cibles = gestionEnnemi.getEnnemisDansRayon(link.getX(), link.getY(), link.getArme().getRayon());
                    link.attaque(cibles);
                    if( link.getArme() instanceof Marteau){
                        tv = new TerrainVue(terrain, tuile);
                        tv.creerCarte();
                    }
                }
                break;
            case K:
                link.changerArmeSuivante();
                System.out.println("Arme actuelle : " + link.getArme());
                break;
            case J:
                link.changerArmePrecedente();
                System.out.println("Arme actuelle : " + link.getArme());
                break;
            case L:
                Arme armeActuelle =  link.getArme();
                link.setArmeActuelle(bombe);
                List<Ennemi> bombCibles = gestionEnnemi.getEnnemisDansRayon(link.getX(), link.getY(), link.getArme().getRayon());
                link.attaque(bombCibles);
                link.setArmeActuelle(armeActuelle);
                break;
            default:
                return;
        }
        miseAJourZoom();
        verifierRencontreGardien();
        System.out.println("Position du personnage: x=" + link.getX() + ", y=" + link.getY());
    }


    public void verifierRencontreGardien() {
        Gardien g = env.verifierRencontreLinkGardien();
        if (g != null) {
                messageVue.afficherDialogueGardien(g, link);
        }
    }


    public void miseAJourZoom(){
        double paneWidth = panneauDeJeu.getWidth();
        double paneHeight = panneauDeJeu.getHeight();
        double linkX = link.getX();
        double linkY = link.getY();

        panneauDeJeu.setTranslateX(-linkX * scaleTransform.getX() + paneWidth / 2);
        panneauDeJeu.setTranslateY(-linkY * scaleTransform.getY() + paneHeight / 2);
    }


    private void initAnimation() {
        gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.1),
                ev -> {
                    env.unTour();
                    miseAJourZoom();

                    coeurVue.mettreAJourCoeurs(link.getPointVie());
                    mettreAJourEtatJeu(); // Appel de la méthode pour mettre à jour l'état du jeu

                }
        );
        gameLoop.getKeyFrames().add(kf);
    }
    public void mettreAJourEtatJeu() {
        // Vérifier si la vie de Link est égale à 0
        if (link.getPointVie() == 0) {
            Main.stopMusicFond();
            afficherEcranGameOver();
            gameLoop.stop();
            System.out.println("Game Over");
        }
        if (env.verifierVictoire()) {
            gameLoop.stop();
            Main.stopMusicFond();
            System.out.println("Victory!");
            afficherEcranVictoire();
        }
    }
    private void afficherEcranGameOver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/lefarwestenperil/sae2_04/vueGameOver.fxml"));
            Parent root = loader.load();
            gameLoop.stop();


            // Récupérer la scène actuelle à partir d'un nœud parent
            Scene scene = panneauDeJeu.getScene();

            // Remplacer le contenu de la scène actuelle par le contenu de la vue Game Over
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Game Over");
            stage.show();

            // Arrêter la musique en cours (si elle est en cours de lecture)
            Main.stopMusicFond();

            // Lancer la musique de défaite
            URL urlImageVaiL = Main.class.getResource("sonPerdue.wav");
            String s = urlImageVaiL.getPath();
            Main.PlayMusicDefaite(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void afficherEcranVictoire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/lefarwestenperil/sae2_04/gagner.fxml"));
            Parent root = loader.load();
            gameLoop.stop();


            // Récupérer la scène actuelle à partir d'un nœud parent
            Scene scene = panneauDeJeu.getScene();

            // Remplacer le contenu de la scène actuelle par le contenu de la vue Victoire
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.setTitle("Victoire");
            stage.show();

            // Arrêter la musique en cours (si elle est en cours de lecture)
            Main.stopMusicFond();

            // Lancer la musique de victoire
            URL urlImageVictoire = Main.class.getResource("sonVictoire.wav");
            String s = urlImageVictoire.getPath();
            Main.PlayMusicVictoire(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reactionBoutonPause(ActionEvent actionEvent) throws IOException {
        pauseGame();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/lefarwestenperil/sae2_04/VuePause.fxml"));
        Parent pauseRoot = loader.load();

        Stage pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.setTitle("Pause Menu");
        pauseStage.setScene(new Scene(pauseRoot));
        pauseStage.showAndWait();
        resumeGame();

        // Méthode pour mettre le jeu en pause
    }


    private void pauseGame() {
        gameLoop.pause();
    }

    // Méthode pour reprendre le jeu
    private void resumeGame() {
        // Reprendre les animations, les timers, etc.
        // Par exemple :
        gameLoop.play();
    }
    public void reactionBoutonPrecedent(ActionEvent actionEvent) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/lefarwestenperil/sae2_04/vueMenu.fxml"));
        root = loader.load();
        // Controleur controleur = loader.getController();
        stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 800);
        stage.setResizable(false);
        stage.setTitle("Le Far West En Péril");
        stage.setScene(scene);

    }

    public void reactionBoutonRecommencer(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/universite_paris8/iut/lefarwestenperil/sae2_04/vue1.fxml"));
        root = loader.load();
        Controleur controleur = loader.getController();
        stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1025, 800);
        stage.setResizable(false);
        stage.setTitle("Le Far West En Péril");
        stage.setScene(scene);
        stage.show();
    }

}

