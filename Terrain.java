
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
public class Terrain 
{
    private Vertex[][] v;
    private int rows;
    private Triangle [] t;
    private int tot_triangles;
    private int width;
    private int x0,y0,z0;
    
    
    public Terrain(int rows, int width)
    {
        this.rows = rows;
        this.width = width;
        v = new Vertex[rows+1][rows+1];
        tot_triangles = this.rows*this.rows*2;
        t = new Triangle[tot_triangles];
        
        x0 = 1200;
        y0 = 300;//250;
        z0 = -600;
        
        init_nodes();
        init_triangles();
        
    }
    
    
    
    public void move_terrain()
    {
        
        Vertex [][] temp = new Vertex[rows+1][rows+1];
        for(int x = 0; x < rows+1; x++)
        {
            for(int z = 0; z < rows; z++)
            {
                double x_cord = v[x][z].get_x();
                double y_cord = v[x][z].get_y();
                double z_cord = v[x][z].get_z()+width;
                temp[x][z+1] = new Vertex(x_cord,y_cord,z_cord);
            }
        }
        
        
        
        Random rand = new Random();
        
        int x_start = x0 - width*rows/2;
        int z_start = z0 - width*rows/2;
        
        for(int x = 0; x < rows+1; x++)
        {
            int x_cord = x_start + x * width;
            int y_cord = y0 + (-width/2 + rand.nextInt((int)width));
            int z_cord = z_start + 0 * width;
            temp[x][0] = new Vertex(x_cord,y_cord,z_cord);
        }
        
        v = temp;
        init_triangles();
         
    }
    
    public void init_triangles()
    {
        tot_triangles = this.rows*this.rows*2;
        t = new Triangle[tot_triangles];
        int t_ix = 0;
        for(int x = 0; x < rows; x++)
        {
            for(int z = 0; z < rows; z++)
            {
                Vertex k0 = v[x+1][z];
                Vertex k1 = v[x][z];
                Vertex k2 = v[x][z+1];
                t[t_ix++] = new Triangle(k0,k1,k2);
                
                k0 = v[x][z+1];
                k1 = v[x+1][z+1];
                k2 = v[x+1][z];
                t[t_ix++] = new Triangle(k0,k1,k2);
            }
        }
    }
    
    
    
    public void init_nodes()
    {
        Random rand = new Random();
        
        int x_start = x0 - width*rows/2;
        int z_start = z0 - width*rows/2;
        
        for(int x = 0;  x < rows+1; x++)
        {
            for(int z = 0; z < rows+1; z++)
            {
                int x_cord = x_start + x * width;
                int y_cord = y0 + (-width/2 + rand.nextInt((int)width));
                int z_cord = z_start + z * width;
                v[x][z] = new Vertex(x_cord,y_cord,z_cord);
            }
        }
    }
    
    public Vertex get_Vertex(int tri, int k)
    {
        return t[tri].get_vertex(k);
    }
    
    public int tot_triangles()
    {
        return tot_triangles;
    }
    
    public Triangle get_triangle(int ix)
    {
        return t[ix];
    }
    
    
}
