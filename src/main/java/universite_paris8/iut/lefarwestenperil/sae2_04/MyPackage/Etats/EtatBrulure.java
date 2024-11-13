package universite_paris8.iut.lefarwestenperil.sae2_04.MyPackage.Etats;

import universite_paris8.iut.lefarwestenperil.sae2_04.MyPackage.Entites.EtreVivants.EtreVivant;

public class EtatBrulure extends Etat {

    public EtatBrulure() {
        super(30);
    }

    @Override
    public void apply(EtreVivant etreVivant) {
        if (getDuree()%10 == 0) {
            etreVivant.seBlesse(1);
            if (isFinish())
                deactivate();
        }
        decreaseDuree();
    }
}
