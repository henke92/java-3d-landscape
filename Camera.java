/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */


public class Camera 
{
    
    public Camera()
    {
        
    }
    
    public double [][] LookAt(Vec3D forward, Vec3D right, Vec3D up, Vec3D pos)
    {
        double [][] matrix = new double[4][4];
        
        matrix[0][0] = forward.x();
        matrix[1][0] = right.x();
        matrix[2][0] = up.x();
        matrix[3][0] = 0;
        
        matrix[0][1] = forward.y();
        matrix[1][1] = right.y();
        matrix[2][1] = up.y();
        matrix[3][1] = 0;
        
        matrix[0][2] = forward.z();
        matrix[1][2] = right.z();
        matrix[2][2] = up.z();
        matrix[3][2] = 0;
        
        Vec3D v = new Vec3D();
        double dotx0 = v.dot_product(pos, forward);
        double dotx1 = v.dot_product(pos, right);
        double dotx2 = v.dot_product(pos, up);
        
        matrix[0][3] = -dotx0;
        matrix[1][3] = -dotx1;
        matrix[2][3] = -dotx2;
        matrix[3][3] = 1;
        
        return matrix;
        
    }
}
