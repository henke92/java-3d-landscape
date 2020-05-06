/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Bullet 
{
    Vec3D pos;
    Vec3D dir;
    double radius;
    
    public Bullet(Vec3D p, Vec3D d, double r)
    {
        pos = p;
        dir = d;
        radius = r;
    }
    
    public Vec3D bullet_dir()
    {
        return this.dir;
    }
    
    public Vec3D bullet_pos()
    {
        return this.pos;
    }
    
    public double get_radius()
    {
        return radius;
    }
    
    public double get_z_dir()
    {
        return dir.z();
    }
    
    public double get_y_dir()
    {
        return dir.y();
    }
    
    public double get_x_dir()
    {
        return dir.x();
    }
    
    public double get_z()
    {
        return pos.z();
    }
    
    public double get_y()
    {
        return pos.y();
    }
    
    public double get_x()
    {
        return pos.x();
    }
    
    public void update()
    {
        pos = pos.add(dir.scale(10));
    }
    
}
