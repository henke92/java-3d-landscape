
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Plane 
{
    private Triangle [] t;
    private Vertex [] v;
    private double x,y,z,w;
    
    private double x_angle;
    private double y_angle;
    private double z_angle;
    
    private boolean rotation;
    
    
    public Plane(Vertex [] v4, boolean rotate,double x,double y,double z)
    {
        
        v = new Vertex[4];
        v[0] = v4[0];
        v[1] = v4[1];
        v[2] = v4[2];
        v[3] = v4[3];
        
        t = new Triangle[4];
        t[0] = new Triangle(v[0],v[1],v[3]);
        t[1] = new Triangle(v[1],v[2],v[3]);
                
                
        rotation = rotate;
        
        if(rotation)
        {
            init_rand_values();
        }
        
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void init_rand_values()
    {
        
        Random rand = new Random();
        x_angle = (double)(-15 + rand.nextInt(31))/1000.0;
        y_angle = (double)(-15 + rand.nextInt(31))/1000.0;
        z_angle = (double)(-15 + rand.nextInt(31))/1000.0;
        
    }
    
    public void set_rotation(boolean value)
    {
        this.rotation = value;
        if(this.rotation)
        {
            init_rand_values();
        }
    }
    
    public boolean is_rotation()
    {
        return this.rotation;
    }
    
    public void set_z_angle(double z)
    {
        this.z_angle = z;
    }
    
    public void set_y_angle(double y)
    {
        this.y_angle = y;
    }
    
    public void set_x_angle(double x)
    {
        this.x_angle = x;
    }
    
    public Vertex get_vertex(int vert)
    {
        return v[vert];
    }
    
    public Vertex get_vertex(int tri, int vert)
    {
        return t[tri].get_vertex(vert);
    }
    
    public Triangle get_triangle(int ix)
    {
        return t[ix];
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
    
    public double get_x()
    {
        return this.x;
    }
    
    public double get_y()
    {
        return this.y;
    }
    
    public double get_z()
    {
        return this.z;
    }
    
    
    public void x_rotate()
    {
            double [][] matrix = 
        {   { 1, 0 , 0},
            { 0, Math.cos(x_angle), - Math.sin(x_angle) },
            { 0, Math.sin(x_angle),   Math.cos(x_angle) },
        };
        
        for(int ix = 0; ix < 4; ix++)
        {
            double [] p = v[ix].points();
            double [] p2 = new double[3];
            
            p[0] = v[ix].get_x() - this.x;
            p[1] = v[ix].get_y() - this.y;
            p[2] = v[ix].get_z() - this.z;
            
            
            for(int y = 0; y < 3; y++)
            {
                double sum = 0;
                for(int x = 0; x < 3; x++)
                {
                    sum += matrix[x][y] * p[x];
                }
                p2[y] = sum;
            }
            p2[0] += this.x;
            p2[1] += this.y;
            p2[2] += this.z;
            
            v[ix].init_points(p2);
        }
    }
    
    public void y_rotate()
    {
           double [][] matrix = 
        {   { Math.cos(y_angle), 0 , Math.sin(y_angle)},
            { 0, 1 , 0 },
            { -Math.sin(y_angle), 0 , Math.cos(y_angle)},
        };
        
        
        for(int ix = 0; ix < 4; ix++)
        {
            double [] p = v[ix].points();
            double [] p2 = new double[3];
            
            p[0] = v[ix].get_x() - this.x;
            p[1] = v[ix].get_y() - this.y;
            p[2] = v[ix].get_z() - this.z;
            
            for(int y = 0; y < 3; y++)
            {
                double sum = 0;
                for(int x = 0; x < 3; x++)
                {
                    sum += matrix[x][y] * p[x];
                }
                p2[y] = sum;
            }
            
            p2[0] += this.x;
            p2[1] += this.y;
            p2[2] += this.z;
            
            
            v[ix].init_points(p2);
        }
    }
    
    public void z_rotate()
    {
        double [][] matrix = 
        {   { Math.cos(z_angle), -Math.sin(z_angle),0},
            { Math.sin(z_angle), Math.cos(z_angle),0},
            { 0 , 0 , 1},
        };
        
        for(int ix = 0; ix < 4; ix++)
        {
            double [] p = v[ix].points();
            double [] p2 = new double[3];
            
            p[0] = v[ix].get_x() - this.x;
            p[1] = v[ix].get_y() - this.y;
            p[2] = v[ix].get_z() - this.z;
            
            for(int y = 0; y < 3; y++)
            {
                double sum = 0;
                for(int x = 0; x < 3; x++)
                {
                    sum += matrix[x][y] * p[x];
                }
                p2[y] = sum;
            }
            
            p2[0] += this.x;
            p2[1] += this.y;
            p2[2] += this.z;
            
            
            v[ix].init_points(p2);
        }
    
    }
}
