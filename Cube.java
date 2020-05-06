
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
public class Cube 
{
    private double x,y,z,width;
    private Vertex [] v;
    private Triangle[] t;
    
    private double x_angle = 0.0;
    private double y_angle = 0.0;
    private double z_angle = 0.0;
    
    private double glob_rotate_x;
    private double glob_rotate_y;
    private double glob_rotate_z;
    
    private double glob_x_angle;
    private double glob_y_angle;
    private double glob_z_angle;
    
    private Vec3D before_rotate_cords;
    private int move_orth_dir = 0;
    private int move_orth_counter = 0;
    private int move_orth_modulo = 1;
    
    Random rand;
    
    public Cube(double x, double y, double z, double w, boolean rotate)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = w;
        
        glob_rotate_x = this.x;
        glob_rotate_y = this.y;
        glob_rotate_z = this.z;
        
        glob_x_angle = glob_y_angle = glob_z_angle = 0;
        
        before_rotate_cords = new Vec3D(this.x,this.y,this.z);
        
        rand = new Random();
        move_orth_dir = rand.nextInt(6);
        move_orth_modulo = 6 + rand.nextInt(11);
        
        if(rotate)
        {
            init_rand_angles();
        }
        
        v = new Vertex[8];
        v[0] = new Vertex(this.x - this.width/2, this.y - this.width/2, this.z - this.width/2);
        v[1] = new Vertex(this.x - this.width/2, this.y + this.width/2, this.z - this.width/2);
        v[2] = new Vertex(this.x + this.width/2, this.y + this.width/2, this.z - this.width/2);
        v[3] = new Vertex(this.x + this.width/2, this.y - this.width/2, this.z - this.width/2);
        v[4] = new Vertex(this.x + this.width/2, this.y - this.width/2, this.z + this.width/2);
        v[5] = new Vertex(this.x + this.width/2, this.y + this.width/2, this.z + this.width/2);
        v[6] = new Vertex(this.x - this.width/2, this.y + this.width/2, this.z + this.width/2);
        v[7] = new Vertex(this.x - this.width/2, this.y - this.width/2, this.z + this.width/2);
        
        t = new Triangle[12];
        t[0] = new Triangle(v[0],v[1],v[2]);
        t[1] = new Triangle(v[2],v[3],v[0]);
        t[2] = new Triangle(v[5],v[6],v[7]);
        t[3] = new Triangle(v[5],v[7],v[4]);
        t[4] = new Triangle(v[6],v[5],v[2]);
        t[5] = new Triangle(v[6],v[2],v[1]);
        t[6] = new Triangle(v[4],v[7],v[0]);
        t[7] = new Triangle(v[4],v[0],v[3]);
        t[8] = new Triangle(v[2],v[5],v[4]);
        t[9] = new Triangle(v[2],v[4],v[3]);
        t[10] = new Triangle(v[6],v[1],v[0]);
        t[11] = new Triangle(v[6],v[0],v[7]);
        
    }
    
    public void init_vertices()
    {
        v = new Vertex[8];
        v[0] = new Vertex(this.x - this.width/2, this.y - this.width/2, this.z - this.width/2);
        v[1] = new Vertex(this.x - this.width/2, this.y + this.width/2, this.z - this.width/2);
        v[2] = new Vertex(this.x + this.width/2, this.y + this.width/2, this.z - this.width/2);
        v[3] = new Vertex(this.x + this.width/2, this.y - this.width/2, this.z - this.width/2);
        v[4] = new Vertex(this.x + this.width/2, this.y - this.width/2, this.z + this.width/2);
        v[5] = new Vertex(this.x + this.width/2, this.y + this.width/2, this.z + this.width/2);
        v[6] = new Vertex(this.x - this.width/2, this.y + this.width/2, this.z + this.width/2);
        v[7] = new Vertex(this.x - this.width/2, this.y - this.width/2, this.z + this.width/2);
    }
    
    
    public void init_triangles()
    {
        t = new Triangle[12];
        t[0] = new Triangle(v[0],v[1],v[2]);
        t[1] = new Triangle(v[2],v[3],v[0]);
        t[2] = new Triangle(v[5],v[6],v[7]);
        t[3] = new Triangle(v[5],v[7],v[4]);
        t[4] = new Triangle(v[6],v[5],v[2]);
        t[5] = new Triangle(v[6],v[2],v[1]);
        t[6] = new Triangle(v[4],v[7],v[0]);
        t[7] = new Triangle(v[4],v[0],v[3]);
        t[8] = new Triangle(v[2],v[5],v[4]);
        t[9] = new Triangle(v[2],v[4],v[3]);
        t[10] = new Triangle(v[6],v[1],v[0]);
        t[11] = new Triangle(v[6],v[0],v[7]);
                
        
    }
    
    private void move_x(double offset)
    {
        this.x = this.x + offset;
        for(int ix = 0; ix < 8; ix++)
        {
            v[ix].set_x(v[ix].get_x() + offset);
        }
        init_triangles();
        glob_rotate_x = glob_rotate_x + offset;
        before_rotate_cords = new Vec3D(this.x,this.y,this.z);
    }
    
    private void move_y(double offset)
    {
        this.y = this.y + offset;
        for(int ix = 0; ix < 8; ix++)
        {
            v[ix].set_y(v[ix].get_y() + offset);
        }
        init_triangles();
        glob_rotate_y = glob_rotate_y + offset;
        before_rotate_cords = new Vec3D(this.x,this.y,this.z);
    }
    
    private void move_z(double offset)
    {
        this.z = this.z + offset;
        for(int ix = 0; ix < 8; ix++)
        {
            v[ix].set_z(v[ix].get_z() + offset);
        }
        init_triangles();
        glob_rotate_z = glob_rotate_z + offset;
        before_rotate_cords = new Vec3D(this.x,this.y,this.z);
    }
    
    public void move_orthogonal()
    {
        double offset = this.width/3;
        
        switch(move_orth_dir)
        {
            case 0:
                move_z(offset);
                break;
            case 1:
                move_x(-offset);
                break;
            case 2:
                move_z(-offset);
                break;
            case 3:
                move_x(offset);
                break;
            case 4:
                move_y(offset);
                break;
            case 5:
                move_y(-offset);
                break;
        }
        
        
        move_orth_counter++;
        if(move_orth_counter % move_orth_modulo == 0)
        {
            if(move_orth_dir == 0)
            {
                move_orth_dir = 2;
            }
            else if(move_orth_dir == 1)
            {
                move_orth_dir = 3;
            }
            else if(move_orth_dir == 2)
            {
                move_orth_dir = 0;
            }
            else if(move_orth_dir == 3)
            {
                move_orth_dir = 1;
            }
            else if(move_orth_dir == 4)
            {
                move_orth_dir = 5;
            }
            else if(move_orth_dir == 5)
            {
                move_orth_dir = 4;
            }
            
            move_orth_modulo = 25+rand.nextInt(30);     
            move_orth_counter = 0;
        }
    }
    
    public void init_global_rotation(Vec3D glob_center)
    {
        
        glob_rotate_x = glob_center.x();
        glob_rotate_y = glob_center.y();
        glob_rotate_z = glob_center.z();
        
        Random rand = new Random();
        glob_x_angle = (double)(-20 + rand.nextInt(41))/1000.0;
        glob_y_angle = (double)(-20 + rand.nextInt(41))/1000.0;
        glob_z_angle = (double)(-20 + rand.nextInt(41))/1000.0;
        
        before_rotate_cords = glob_center;
        
      
    }
    
    public void reset_global_rotation()
    {
        this.x = before_rotate_cords.x();
        this.y = before_rotate_cords.y();
        this.z = before_rotate_cords.z();
        
        glob_rotate_x = this.x;
        glob_rotate_y = this.y;
        glob_rotate_z = this.z;
        
        set_rot_angles_zero();
        init_rand_angles();
    }
    
    public void set_glob_cords_zero()
    {
        glob_rotate_x = this.x;
        glob_rotate_y = this.y;
        glob_rotate_z = this.z;
    }
    
    public void set_rot_angles_zero()
    {
        glob_x_angle = glob_y_angle = glob_z_angle = 0;
    }
    
    public void init_rand_angles()
    {
        Random rand = new Random();
        x_angle = (double)(-20 + rand.nextInt(41))/1000.0;
        y_angle = (double)(-20 + rand.nextInt(41))/1000.0;
        z_angle = (double)(-20 + rand.nextInt(41))/1000.0;
    }
    
    public void reset_angles()
    {
        x_angle = y_angle = z_angle = 0;
    }
    
    public Triangle get_triangle(int ix)
    {
        return t[ix];
    }
    
    public void set_z_rotate(double z)
    {
        z_angle = z;
    }
    
    public void set_y_rotate(double y)
    {
        y_angle = y;
    }
    
    public void set_x_rotate(double x)
    {
        x_angle = x;
    }
    
    public void set_cube_cent_z(double z)
    {
        this.z = z;
    }
    
    public void set_cube_cent_y(double y)
    {
        this.y = y;
    }
    
    public void set_cube_cent_x(double x)
    {
        this.x = x;
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
    
    public double x_angle()
    {
        return this.x_angle;
    }
    
    public double y_angle()
    {
        return this.y_angle;
    }
    
    public double z_angle()
    {
        return this.z_angle;
    }
    
    public int get_cube_orth_dir()
    {
        return this.move_orth_dir;
    }
    
    public void set_cube_orth_dir(int dir)
    {
        this.move_orth_dir = dir;
    }
    
    public void global_x_rotate()
    {
        double [][] matrix = 
        {   { 1, 0 , 0},
            { 0, Math.cos(glob_x_angle), - Math.sin(glob_x_angle) },
            { 0, Math.sin(glob_x_angle),   Math.cos(glob_x_angle) },
        };
        
        for(int ix = 0; ix < 8; ix++)
        {
            double [] p = v[ix].points();
            double [] p2 = new double[3];
            
            
            p[0] = v[ix].get_x() - glob_rotate_x;
            p[1] = v[ix].get_y() - glob_rotate_y;
            p[2] = v[ix].get_z() - glob_rotate_z;
            
            
            for(int y = 0; y < 3; y++)
            {
                double sum = 0;
                for(int x = 0; x < 3; x++)
                {
                    sum += matrix[x][y] * p[x];
                }
                p2[y] = sum;
            }
            p2[0] += glob_rotate_x;
            p2[1] += glob_rotate_y;
            p2[2] += glob_rotate_z;
            
            v[ix].init_points(p2);
        }
    }
    
    public void global_y_rotate()
    {
        double [][] matrix = 
        {   { Math.cos(glob_y_angle), 0 , Math.sin(glob_y_angle)},
            { 0, 1 , 0 },
            { -Math.sin(glob_y_angle), 0 , Math.cos(glob_y_angle)},
        };
        
        
        
        
        for(int ix = 0; ix < 8; ix++)
        {
            double [] p = v[ix].points();
            double [] p2 = new double[3];
            
            p[0] = v[ix].get_x() - glob_rotate_x;
            p[1] = v[ix].get_y() - glob_rotate_y;
            p[2] = v[ix].get_z() - glob_rotate_z;
            
            for(int y = 0; y < 3; y++)
            {
                double sum = 0;
                for(int x = 0; x < 3; x++)
                {
                    sum += matrix[x][y] * p[x];
                }
                p2[y] = sum;
            }
            
            p2[0] += glob_rotate_x;
            p2[1] += glob_rotate_y;
            p2[2] += glob_rotate_z;
            v[ix].init_points(p2);
        }
    }
    
    public void global_z_rotate()
    {
        double [][] matrix = 
        {   { Math.cos(glob_z_angle), -Math.sin(glob_z_angle),0},
            { Math.sin(glob_z_angle), Math.cos(glob_z_angle),0},
            { 0 , 0 , 1},
        };
        
        for(int ix = 0; ix < 8; ix++)
        {
            double [] p = v[ix].points();
            double [] p2 = new double[3];
            
            p[0] = v[ix].get_x() - glob_rotate_x;
            p[1] = v[ix].get_y() - glob_rotate_y;
            p[2] = v[ix].get_z() - glob_rotate_z;
            
            for(int y = 0; y < 3; y++)
            {
                double sum = 0;
                for(int x = 0; x < 3; x++)
                {
                    sum += matrix[x][y] * p[x];
                }
                p2[y] = sum;
            }
            
            p2[0] += glob_rotate_x;
            p2[1] += glob_rotate_y;
            p2[2] += glob_rotate_z;
            
            
            v[ix].init_points(p2);
        }
    }
    
    public void x_rotate()
    {
            double [][] matrix = 
        {   { 1, 0 , 0},
            { 0, Math.cos(x_angle), - Math.sin(x_angle) },
            { 0, Math.sin(x_angle),   Math.cos(x_angle) },
        };
        
        for(int ix = 0; ix < 8; ix++)
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
        
        
        for(int ix = 0; ix < 8; ix++)
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
        
        for(int ix = 0; ix < 8; ix++)
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
    
    public Triangle[] get_triangles()
    {
        return t;
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
    
    public double x0()
    {
        return this.x - this.width/2;
    }
    
    public double x1()
    {
        return this.x + this.width/2;
    }
    
    public double y0()
    {
        return this.y - this.width/2;
    }
    
    public double y1()
    {
        return this.y + this.width/2;
    }
    
    public double z0()
    {
        return this.z - this.width/2;
    }
    
    public double z1()
    {
        return this.z + this.width/2;
    }
}
