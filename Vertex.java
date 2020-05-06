/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Vertex 
{
    private double x,y,z;
    public Vertex(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void init_points(double [] p)
    {
        this.x = p[0];
        this.y = p[1];
        this.z = p[2];
    }
    
    public double [] points()
    {
        double [] p = new double[3];
        p[0] = this.x;
        p[1] = this.y;
        p[2] = this.z;
        return p;
    }
    
    public double get_z()
    {
        return this.z;
    }
    
    public double get_y()
    {
        return this.y;
    }
    
    public double get_x()
    {
        return this.x;
    }
    
    public void set_z(double z)
    {
        this.z = z;
    }
    
    public void set_y(double y)
    {
        this.y = y;
    }
    
    public void set_x(double x)
    {
        this.x = x;
    }
    
}
