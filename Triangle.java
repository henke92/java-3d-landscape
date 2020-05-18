/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Triangle 
{
    private Vertex [] v;
    public Triangle(Vertex [] v0)
    {
        v = new Vertex[3];
        for(int ix = 0; ix < 3;ix++)
        {
            v[ix] = v0[ix];
        }
    }
    
    public Triangle(Vertex v0, Vertex v1, Vertex v2)
    {
        v = new Vertex[3];
        v[0] = v0;
        v[1] = v1;
        v[2] = v2;
    }
    
    public double z_norm_normal()
    {
        double normal = z_normal();
        double size = Math.sqrt((Math.pow(x_normal(),2) + Math.pow(y_normal(),2) + Math.pow(z_normal(),2)));
        normal *= 1/size;
        return normal;          
    }
    
    public double z_normal()
    {
        double z_normal = 0;
        double Ax = v[2].get_x() - v[1].get_x();
        double Ay = v[2].get_y() - v[1].get_y();
        double Az = v[2].get_z() - v[1].get_z();
        
        double Bx = v[0].get_x() - v[1].get_x();
        double By = v[0].get_y() - v[1].get_y();
        double Bz = v[0].get_z() - v[1].get_z();
        z_normal = Ax * By - Ay * Bx;
        
        return z_normal;
    }
    
    public double y_norm_normal()
    {
        double normal = y_normal();
        double size = Math.sqrt((Math.pow(x_normal(),2) + Math.pow(y_normal(),2) + Math.pow(z_normal(),2)));
        normal *= 1/size;
        return normal;
    }
    
    public double y_normal()
    {
        double y_normal = 0;
        double Ax = v[2].get_x() - v[1].get_x();
        double Ay = v[2].get_y() - v[1].get_y();
        double Az = v[2].get_z() - v[1].get_z();
        
        double Bx = v[0].get_x() - v[1].get_x();
        double By = v[0].get_y() - v[1].get_y();
        double Bz = v[0].get_z() - v[1].get_z();
        y_normal = Az * Bx - Ax * Bz;
        
        return y_normal;
    }
    
    public double x_norm_normal()
    {
        double normal = x_normal();
        double size = Math.sqrt((Math.pow(x_normal(),2) + Math.pow(y_normal(),2) + Math.pow(z_normal(),2)));
        normal *= 1/size;
        return normal;
    }
    
    public double x_normal()
    {
        double x_normal = 0;
        double Ax = v[2].get_x() - v[1].get_x();
        double Ay = v[2].get_y() - v[1].get_y();
        double Az = v[2].get_z() - v[1].get_z();
        
        double Bx = v[0].get_x() - v[1].get_x();
        double By = v[0].get_y() - v[1].get_y();
        double Bz = v[0].get_z() - v[1].get_z();
        x_normal = Ay * Bz - Az * By;

        return x_normal;
    }
    
    public Vertex get_vertex(int ix)
    {
        return v[ix];
    }
    
    public void set_vertex_z(int ix, double val)
    {
        v[ix].set_z(val);
    }
    
    public void set_vertex_y(int ix, double val)
    {
        v[ix].set_y(val);
    }
    
    public void set_vertex_x(int ix, double val)
    {
        v[ix].set_x(val);
    }
    
    public double get_vertex_z(int ix)
    {
        return v[ix].get_z();
    }
    
    public double get_vertex_y(int ix)
    {
        return v[ix].get_y();
    }
    
    public double get_vertex_x(int ix)
    {
        return v[ix].get_x();
    }
    
}
