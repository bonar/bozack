
package bozack;

public enum ChordRole {
    TONIC,
    SUB_DOMINANT,
    DOMINANT;

    public static ChordRole rotate(ChordRole role) {
        switch (role) {
            case TONIC:
                return SUB_DOMINANT;
            case SUB_DOMINANT:
                return DOMINANT;
            case DOMINANT:
                return TONIC;
        }
        return null;
    }
}

