/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Vec3D 
{
    private double [] v;
    public Vec3D(double x0, double y0, double z0)
    {
        v = new double[3];
        v[0] = x0;
        v[1] = y0;
        v[2] = z0;
    }
    
    public Vec3D()
    {
        v = new double[3];
        v[0] = v[1] = v[2] = 0;
    }
    
    Vec3D normalize()
    {
        Vec3D normalized = new Vec3D();
        double length = Math.sqrt((Math.pow(v[0],2)+Math.pow(v[1],2)+Math.pow(v[2],2)));
        if(length != 0)
        {
            double x0 = v[0] / length;
            double y0 = v[1] / length;
            double z0 = v[2] / length;
            normalized = new Vec3D(x0,y0,z0);
        }
        
        return normalized;
    }
    
    
    
    Vec3D scale(double sc)
    {
        Vec3D v0 = new Vec3D(v[0]*sc,v[1]*sc,v[2]*sc);
        return v0;
    }
    
    Vec3D multiply(Vec3D v1)
    {
        double x0 = v1.x() * v[0];
        double y0 = v1.y() * v[1];
        double z0 = v1.z() * v[2];
        return new Vec3D(x0,y0,z0);
    }
    
    Vec3D subtract(Vec3D v1)
    {
        double x0 = v[0] - v1.x();
        double y0 = v[1] - v1.y();
        double z0 = v[2] - v1.z();
        return new Vec3D(x0,y0,z0);
    }
    
    Vec3D add(Vec3D v1)
    {
        double x0 = v[0] + v1.x();
        double y0 = v[1] + v1.y();
        double z0 = v[2] + v1.z();
        return new Vec3D(x0,y0,z0);
    }
    
    Vec3D cross_product(Vec3D v0, Vec3D v1)
    {
        double a1 = v0.x();
        double a2 = v0.y();
        double a3 = v0.z();
        
        double b1 = v1.x();
        double b2 = v1.y();
        double b3 = v1.z();
        
        double c1 = a2 * b3 - a3 * b2;
        double c2 = a3 * b1 - a1 * b3;
        double c3 = a1 * b2 - a2 * b1;
        
        Vec3D cross_pr = new Vec3D(c1,c2,c3);
        return cross_pr;
    }
    
    Vec3D cross_product(Vec3D v1)
    {
        double b1 = v[0];
        double b2 = v[1];
        double b3 = v[2];
        
        double a1 = v1.x();
        double a2 = v1.y();
        double a3 = v1.z();
        
        double c1 = a2 * b3 - a3 * b2;
        double c2 = a3 * b1 - a1 * b3;
        double c3 = a1 * b2 - a2 * b1;
        
        Vec3D cross_pr = new Vec3D(c1,c2,c3);
        return cross_pr;
    }
    
    double dist(Vec3D v0, Vec3D v1)
    {
        double dx = Math.abs(v1.x() - v0.x());
        double dy = Math.abs(v1.y() - v0.y());
        double dz = Math.abs(v1.z() - v0.z());
        return Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
        
    }
    
    double dot_product(Vec3D v0, Vec3D v1)
    {
        double pr = 0;
        for(int ix = 0; ix < 3; ix++)
        {
            pr += v0.vec_ix(ix) * v1.vec_ix(ix);
        }
        return pr;
    }
    
    double dot_product(Vec3D v1)
    {
        double pr = 0;
        for(int ix = 0; ix < 3; ix++)
        {
            pr += this.vec_ix(ix) * v1.vec_ix(ix);
        }
        return pr;
    }
    
    public double vec_ix(int ix)
    {
        return v[ix];
    }
    
    public double z()
    {
        return v[2];
    }
    
    public double y()
    {
        return v[1];
    }
    
    public double x()
    {
        return v[0];
    }
    
    public double [] values()
    {
        return v;
    }
    
    public String info()
    {
        return String.valueOf(v[0]+" "+v[1]+" "+v[2]);
    }
    
    
    
}
