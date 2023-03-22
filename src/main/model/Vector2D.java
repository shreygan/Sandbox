package model;

public class Vector2D  {

    private double vx;
    private double vy;

    public Vector2D(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public Vector2D(Vector2D v) {
        this.vx = v.vx;
        this.vy = v.vy;
    }

    public void rotateClockwise(double angle) {
        double tempv1 = this.vx * Math.cos(angle) + this.vy * Math.sin(angle);
        double tempv2 = this.vx * - Math.sin(angle) + this.vy * Math.cos(angle);

        this.vx = tempv1;
        this.vy = tempv2;
    }

    public void rotateCounterClockwise(double angle) {
        double tempv1 = this.vx * Math.cos(angle) - this.vy * Math.sin(angle);
        double tempv2 = this.vx * Math.sin(angle) + this.vy * Math.cos(angle);

        this.vx = tempv1;
        this.vy = tempv2;
    }

    public void multiplyBy(double x) {
        this.vx *= x;
        this.vy *= x;
    }

    public double getSum() {
        return Math.abs(vx) + Math.abs(vy);
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
}
