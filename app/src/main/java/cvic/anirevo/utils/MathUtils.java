package cvic.anirevo.utils;

public class MathUtils {

    public static float angleBetweenLines (float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float angle1 = (float) Math.atan2( (fY - sY), (fX - sX) );
        float angle2 = (float) Math.atan2( (nfY - nsY), (nfX - nsX) );

        float angle = ((float)Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return angle;
    }

    public static float scaleBetweenPoints (float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float dist1 = distanceBetweenPoints(fX, fY, sX, sY);
        float dist2 = distanceBetweenPoints(nfX, nfY, nsX, nsY);

        return (dist2 / dist1) - 1;
    }

    private static float distanceBetweenPoints (float x1, float y1, float x2, float y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return (float) Math.sqrt(dx*dx + dy*dy);
    }

}
