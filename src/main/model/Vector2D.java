package model;

public class Vector2D  {

    private double v1;
    private double v2;

    public Vector2D(double v1, double v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public void rotateClockwise(double angle) {
        double tempv1 = this.v1 * Math.cos(angle) + this.v2 * Math.sin(angle);
        double tempv2 = this.v1 * - Math.sin(angle) + this.v2 * Math.cos(angle);

        this.v1 = tempv1;
        this.v2 = tempv2;
    }

    public void rotateCounterClockwise(double angle) {
        double tempv1 = this.v1 * Math.cos(angle) - this.v2 * Math.sin(angle);
        double tempv2 = this.v1 * Math.sin(angle) + this.v2 * Math.cos(angle);

        this.v1 = tempv1;
        this.v2 = tempv2;
    }

    public void multiplyBy(double x) {
        this.v1 *= x;
        this.v2 *= x;
    }

    public double getSum() {
        return Math.abs(v1) + Math.abs(v2);
    }

    public double getV1() {
        return v1;
    }

    public void setV1(double v1) {
        this.v1 = v1;
    }

    public double getV2() {
        return v2;
    }

    public void setV2(double v2) {
        this.v2 = v2;
    }
}
