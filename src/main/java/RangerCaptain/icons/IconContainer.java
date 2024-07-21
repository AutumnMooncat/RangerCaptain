package RangerCaptain.icons;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.CarrotMod;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;

public class IconContainer {
    public static class CarrotIcon extends AbstractCustomIcon {
        static AbstractCustomIcon singleton;

        public CarrotIcon() {
            super(MainModfile.makeID("Carrot"), CarrotMod.modIcon);
        }

        public static AbstractCustomIcon get() {
            if (singleton == null) {
                singleton = new CarrotIcon();
            }
            return singleton;
        }
    }
}
