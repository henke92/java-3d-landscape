
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
public class Pyramid 
{
    private double x,y,z,width;
    private Vertex [] v;
    private Triangle[] t;
    
    private double x_angle = 0.0;
    private double y_angle = 0.0;
    private double z_angle = 0.0;
    
    Random rand;
    
    public Pyramid(double x, double y, double z, double w, boolean rot)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = w;
        
        
        if(rot)
        {
            init_rand_angles();
        }
        
        init_vertices();
        init_triangles();
        
    }
    
    public void init_rand_angles()
    {
        Random rand = new Random();
        x_angle = (double)(-20 + rand.nextInt(41))/1000.0;
        y_angle = (double)(-20 + rand.nextInt(41))/1000.0;
        z_angle = (double)(-20 + rand.nextInt(41))/1000.0;
    }
    
    
    public void init_vertices()
    {
        v = new Vertex[5];
        v[0] = new Vertex(x,y-width*2/4,z);
        v[1] = new Vertex(x+this.width/2,y+this.width/2,z+this.width/2);
        v[2] = new Vertex(x-this.width/2,y+this.width/2,z+this.width/2);
        v[3] = new Vertex(x-this.width/2,y+this.width/2,z-this.width/2);
        v[4] = new Vertex(x+this.width/2,y+this.width/2,z-this.width/2);
    }
    
    
    public void init_triangles()
    {
        t = new Triangle[6];
        
        t[0] = new Triangle(v[1],v[2],v[0]); 
        t[1] = new Triangle(v[2],v[3],v[0]); 
        t[2] = new Triangle(v[3],v[4],v[0]);  
        t[3] = new Triangle(v[4],v[1],v[0]); 
        t[4] = new Triangle(v[1],v[3],v[2]); 
        t[5] = new Triangle(v[3],v[1],v[4]); 
        
        

    }
    
    public Triangle[] get_triangles()
    {
        return t;
    }
    
    public Triangle get_triangle(int ix)
    {
        return t[ix];
    }
    
    public double get_cube_cent_z()
    {
        return this.z;
    }
    
    public double get_cube_cent_y()
    {
        return this.y;
    }
    
    public double get_cube_cent_x()
    {
        return this.x;
    }
    
    public void x_rotate()
    {
            double [][] matrix = 
        {   { 1, 0 , 0},
            { 0, Math.cos(x_angle), - Math.sin(x_angle) },
            { 0, Math.sin(x_angle),   Math.cos(x_angle) },
        };
        
        for(int ix = 0; ix < 5; ix++)
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
        
        
        for(int ix = 0; ix < 5; ix++)
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
        
        for(int ix = 0; ix < 5; ix++)
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
    
    public void set_triangles(Triangle [] tri)
    {
        t = tri;
    }
    
    public double get_width()
    {
        return this.width;
    }
    
    public Vertex get_Vertex(int tri, int k)
    {
        return t[tri].get_vertex(k);
    }
    
    public double get_Vertex_z(int tri, int vert)
    {
        return t[tri].get_vertex_z(vert);
    }
    
    public double get_Vertex_y(int tri, int vert)
    {
        return t[tri].get_vertex_y(vert);
    }
    
    public double get_Vertex_x(int tri, int vert)
    {
        return t[tri].get_vertex_x(vert);
    }
    
    public void set_Vertex_z(int tri, int vert, double val)
    {
        t[tri].set_vertex_z(vert, val);
    }
    
    public void set_Vertex_y(int tri, int vert, double val)
    {
        t[tri].set_vertex_y(vert, val);
    }
    
    public void set_Vertex_x(int tri, int vert, double val)
    {
        t[tri].set_vertex_x(vert, val);
    }
    
    public Vertex [] v()
    {
        return v;
    }
    
}
