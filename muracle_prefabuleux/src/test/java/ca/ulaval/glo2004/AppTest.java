package ca.ulaval.glo2004;

import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Exemple de test unitaire utilisant JUnit et Google Truth. Vous n'avez pas a faire des tests dans le cadre de votre
 * projet. Par contre, il peut s'averer utile de faire quelques tests pour certaines method pour s'assurer qu'elle retourne
 * bien ce qu'elle devrait retourner
 */
public class AppTest 
{
    /**
     * Exemple de test avec JUnit
     */
    @Test
    public void exempleDuneAssertionJUnit()
    {
        assertTrue( true );
        assertFalse(false);
    }

    /**
     * Exemple de test avec Google truth
     */
    @Test
    public void exempleDuneAssertionGoogleTruth() {
        assertThat(true).isTrue();
        assertThat(false).isFalse();
        assertThat(1).isEqualTo(1);
        assertThat('x').isNotEqualTo('y');
        assertThat(10).isGreaterThan(5);
    }
    
    @Test
    public void divisionImperial() {
        Imperial imp1 = new Imperial(1);
        Imperial imp2 = new Imperial(2);
        Imperial resultatAttendu = new Imperial(0,1,2);
        Imperial resultatDivision = Imperial.divide(imp1, imp2);
        assertThat(resultatDivision.getEntier()).isEqualTo(resultatAttendu.getEntier());
        assertThat(resultatDivision.getNumerateur()).isEqualTo(resultatAttendu.getNumerateur());
        assertThat(resultatDivision.getDenominateur()).isEqualTo(resultatAttendu.getDenominateur());
    }
//    
    @Test
    public void divisionFractionImperial() {
        Imperial imp1 = new Imperial(0,1,2);
        Imperial imp2 = new Imperial(2);
        Imperial resultatAttendu = new Imperial(0,1,4);
        Imperial resultatDivision = Imperial.divide(imp1, imp2);
        assertThat(resultatDivision.getEntier()).isEqualTo(resultatAttendu.getEntier());
        assertThat(resultatDivision.getNumerateur()).isEqualTo(resultatAttendu.getNumerateur());
        assertThat(resultatDivision.getDenominateur()).isEqualTo(resultatAttendu.getDenominateur());
    }
    
    @Test
    public void divisionImperialConvertionEnPixel() {
        Imperial imp1 = new Imperial(1);
        Imperial imp2 = new Imperial(2);
        double resultatDivisionEnPixel = Imperial.divide(imp1, imp2).getDoublePixelValue();
        double resultatAttenduEnPixel = 128 / 2;
        assertThat(resultatDivisionEnPixel).isEqualTo(resultatAttenduEnPixel);
    }
    
    @Test
    public void multiplicationImperialConvertionEnPixel() {
        Imperial imp1 = new Imperial(1);
        Imperial imp2 = new Imperial(2);
        double resultatDivisionEnPixel = Imperial.multiply(imp1, imp2).getDoublePixelValue();
        double resultatAttenduEnPixel = 128*2;
        assertThat(resultatDivisionEnPixel).isEqualTo(resultatAttenduEnPixel);
    }
    
    @Test
    public void divisionNegatifImperial() {
        Imperial imp1 = new Imperial(-2);
        Imperial imp2 = new Imperial(0,1,2);
        Imperial resultatDivision = Imperial.divide(imp1, imp2);
        Imperial resultatAttendu = new Imperial(-4);
        assertThat(resultatDivision.getEntier()).isEqualTo(resultatAttendu.getEntier());
        assertThat(resultatDivision.getNumerateur()).isEqualTo(resultatAttendu.getNumerateur());
        assertThat(resultatDivision.getDenominateur()).isEqualTo(resultatAttendu.getDenominateur());
    }
    
    @Test
    public void multiplicationNegatifImperial() {
        Imperial imp1 = new Imperial(-2);
        Imperial imp2 = new Imperial(0,1,2);
        Imperial resultatDivision = Imperial.multiply(imp1, imp2);
        Imperial resultatAttendu = new Imperial(-1);
        assertThat(resultatDivision.getEntier()).isEqualTo(resultatAttendu.getEntier());
        assertThat(resultatDivision.getNumerateur()).isEqualTo(resultatAttendu.getNumerateur());
        assertThat(resultatDivision.getDenominateur()).isEqualTo(resultatAttendu.getDenominateur());
    }
    
    @Test
    public void multiplicationNegatifImperial2() {
        Imperial imp1 = new Imperial(-111,3,5);
        Imperial imp2 = new Imperial(-1);
        Imperial resultatMultiplication = Imperial.multiply(imp1, imp2);
        Imperial resultatAttendu = new Imperial(111,3,5);
        assertThat(resultatMultiplication.getEntier()).isEqualTo(resultatAttendu.getEntier());
        assertThat(resultatMultiplication.getNumerateur()).isEqualTo(resultatAttendu.getNumerateur());
        assertThat(resultatMultiplication.getDenominateur()).isEqualTo(resultatAttendu.getDenominateur());
    }
    
    @Test
    public void pixelVersImperial() {
        Imperial imp1 = Imperial.getImperialFromPixelValue(200);
        Imperial resultatAttenduEnImperial = new Imperial(1,9,16);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void pixelVersImperial2() {
        Imperial imp1 = Imperial.getImperialFromPixelValue(320);
        Imperial resultatAttenduEnImperial = new Imperial(2,1,2);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void pixelVersImperial3() {
        Imperial imp1 = Imperial.getImperialFromPixelValue(345.6);
        Imperial resultatAttenduEnImperial = new Imperial(2,7,10);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void pixelVersImperial4() {
        Imperial imp1 = Imperial.getImperialFromPixelValue(2342);
        Imperial resultatAttenduEnImperial = new Imperial(18,19,64);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void pixelVersImperial5() {
        Imperial imp1 = Imperial.getImperialFromPixelValue(42);
        Imperial resultatAttenduEnImperial = new Imperial(0,21,64);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void pixelVersImperial6() {
        Imperial imp1 = Imperial.getImperialFromPixelValue(127);
        Imperial resultatAttenduEnImperial = new Imperial(0,127,128);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void pixelVersImperial7() {
        Imperial imp1 = Imperial.getImperialFromPixelValue(-12800);
        Imperial resultatAttenduEnImperial = new Imperial(-100,0,1);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void imperialVersPixel() {
        Imperial impVal = new Imperial(-105,1,2);
        double resultInPixels = impVal.getDoublePixelValue();
        double resultShouldBe = -13504;
        assertThat(resultInPixels).isEqualTo(resultShouldBe);
    }

    @Test
    public void additionImperial() {
        Imperial imp1 = Imperial.add(Imperial.add(Imperial.add(new Imperial(), new Imperial(200)),new Imperial(5,1,2)), new Imperial(5,1,2));
        Imperial resultatAttenduEnImperial = new Imperial(211);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void additionImperial2() {
        Imperial imp1 = new Imperial(-104,1,2);
        Imperial imp2 = new Imperial(104,1,2);
        Imperial result = imp1.add(imp2);
        Imperial resultatAttenduEnImperial = new Imperial(0);
        assertThat(result.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(result.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(result.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void addZero() {
        Imperial imp1 = new Imperial(-104,1,2);
        Imperial imp2 = new Imperial(0);
        Imperial result = imp1.add(imp2);
        Imperial resultatAttenduEnImperial = new Imperial(-104,1,2);
        assertThat(result.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(result.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(result.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void soustractionImperial() {
        Imperial imp1 = Imperial.substract(new Imperial(), new Imperial(42));
        Imperial resultatAttenduEnImperial = new Imperial(-42);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void soustractionImperial2() {
        Imperial imp1 = Imperial.substract(new Imperial(23,8,10), new Imperial(42,7,9));
        Imperial resultatAttenduEnImperial = new Imperial(-18,44,45);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void soustractionImperial3() {
        Imperial imp1 = Imperial.substract(new Imperial(-23,1,5), new Imperial(-42,1,8));
        Imperial resultatAttenduEnImperial = new Imperial(18,37,40);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
    
    @Test
    public void soustractionImperial4() {
        Imperial imp1 = Imperial.substract(new Imperial(-2,51,128), new Imperial(1));
        Imperial resultatAttenduEnImperial = new Imperial(-3,51,128);
        assertThat(imp1.getEntier()).isEqualTo(resultatAttenduEnImperial.getEntier());
        assertThat(imp1.getNumerateur()).isEqualTo(resultatAttenduEnImperial.getNumerateur());
        assertThat(imp1.getDenominateur()).isEqualTo(resultatAttenduEnImperial.getDenominateur());
    }
}
