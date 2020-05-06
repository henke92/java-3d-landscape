
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JScrollBar;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class frame_3d extends javax.swing.JFrame {

    private Timer t1;
    private int delay = 0;
    private int FRAME_WIDTH;
    private Graphics g;
    private double pr_angle = Math.PI/2;
    
    double z_far = 500.0;   //500.0
    double z_near = -100.0;  //100.0
    
    private Random rand; 
    
    private Cube [] c;
    private int nr_of_cubes;
    private int cubes_size;
    private double add_cube_width = 80;
    private boolean add_cube_rotation = false;
    private boolean is_global_rotation = false;
    private boolean add_cube_orthogonal = true;
    private boolean fill_transparency = false;
    
    private Plane [] pl;
    private boolean plane_rotation = false;
    private double plane_w = 300;
    private double pl_x_offset = plane_w;
    private double pl_y_offset = 0;
    
    private Camera cam;
    private Vec3D light;

    private Vec3D pos;
    private Vec3D forward,right,up;
    private double mouse_sens = 0.7;
    
   
    private double yaw = -Math.PI/2;
    private double pitch = Math.PI/64;
    
    private int lastx = 300;
    private int lasty = 300;
    private int curx = -1;
    private int cury = -1;
    
    private boolean left_pressed;
    private boolean right_pressed;
    private boolean up_pressed;
    private boolean down_pressed;
    private boolean forw_pressed;
    private boolean back_pressed;
    private boolean cam_movement = true;
    
    private Bullet [] b;
    private int nr_of_bullets;
    private int bullet_size;
    private int bullet_counter = 0;
    private int bullet_modulo = 2;
    
    private boolean init_cube_wall = true;
    private boolean init_rand_cubes = true;
    private boolean init_temple_wall = false;
    private boolean draw_lines = false;
    private boolean add_grid = true;
    
    private double dy = 25;
    
    private boolean move_cubes_orth = false;
    private int orth_counter = 0;
    private int orth_modulo = 15;
    
    
    
    /**
     * Creates new form frame_3d
     */
    public frame_3d() {
        initComponents();
        init();
        init_cubes();
        init_camera();
        init_light();
        init_bullets();
        init_planes();
        act_perf(delay);
    }
    
    
    public void jump()
    {
        dy = -30;
    }
    
    public void update_jump()
    {
        if(dy < 30)
        {
            dy += 0.75;
            pos = pos.add(new Vec3D(0,dy,0));
        }
    }
    
    public void add_bullet(Vec3D p, Vec3D d, double rad)
    {
        if(nr_of_bullets < bullet_size)
        {
            b[nr_of_bullets++] = new Bullet(p,d,rad);
        }
    }
    
    public void init_bullets()
    {
        nr_of_bullets = 0;
        bullet_size = 50;
        b = new Bullet[bullet_size];
    }
    
    public void expand_cubes()
    {
        cubes_size += 10;
        Cube [] c2 = new Cube[cubes_size];
        for(int ix = 0; ix < cubes_size; ix++)
        {
            c2[ix] = null;
        }
        for(int ix = 0; ix < nr_of_cubes; ix++)
        {
            c2[ix] = c[ix];
        }
        c = c2;
    }
    
    public boolean find_cube(double x, double y, double z)
    {
        boolean found = false;
        for(int ix = 0; ix < nr_of_cubes; ix++)
        {
            if(c[ix].get_cube_cent_x() == x &&
                    c[ix].get_cube_cent_y() == y &&
                    c[ix].get_cube_cent_z() == z)
            {
                found = true;
            }
            
        }
        return found;
    }
    
    public void add_cube(double x, double y, double z, double w, boolean rot)
    {
        if(nr_of_cubes >= cubes_size)
        {
            expand_cubes();   
        }
        
        if(add_grid && !rot)
        {
            int x_val = (int)(x - ((int)(x) % (int)(w)));
            int y_val = (int)(y - ((int)(y) % (int)(w)));
            int z_val = (int)(z - ((int)(z) % (int)(w)));
            if(!find_cube(x_val,y_val,z_val))
            {
                c[nr_of_cubes++] = new Cube(x_val,y_val,z_val,w,rot);
            }
        }
        else if(add_cube_orthogonal)
        {
            c[nr_of_cubes++] = new Cube(x,y,z,w,rot);
        }
        else if(!add_cube_orthogonal)
        {
            c[nr_of_cubes++] = new Cube(x,y,z,w,rot);
            boolean rotate = false;
            if(c[nr_of_cubes-1].x_angle() != 0)
            {
                rotate  = true;
            }
            if(c[nr_of_cubes-1].y_angle() != 0)
            {
                rotate = true;
            }
            if(c[nr_of_cubes-1].z_angle() != 0)
            {
                rotate = true;
            }
            c[nr_of_cubes-1].init_rand_angles();
            for(int ix = 0; ix < 100; ix++)
            {
                c[nr_of_cubes-1].x_rotate();
                c[nr_of_cubes-1].y_rotate();
                c[nr_of_cubes-1].z_rotate();
            }
            if(!rotate)
            {
                c[nr_of_cubes-1].reset_angles();
            }
        }
        
    }
    
    public void rotate_cubes(Vec3D pos)
    {
        for(int ix = 0; ix < nr_of_cubes; ix++)
        {
            double dx = pos.x() - c[ix].get_cube_cent_x();
            double dy = pos.y() - c[ix].get_cube_cent_y();
            double dz = pos.z() - c[ix].get_cube_cent_z();
            double d = Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
            
            if(d < 100)
            {
                if(c[ix].x_angle() != 0 || c[ix].y_angle() != 0 || c[ix].z_angle() != 0)
                {
                    c[ix].reset_angles();
                }
                else
                {
                    c[ix].init_rand_angles();
                }
            }
        }
    }
    
    public void remove_cubes(Vec3D pos)
    {
        for(int ix = 0; ix < nr_of_cubes; ix++)
        {
            double dx = pos.x() - c[ix].get_cube_cent_x();
            double dy = pos.y() - c[ix].get_cube_cent_y();
            double dz = pos.z() - c[ix].get_cube_cent_z();
            double d = Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
            
            if(d < 100)
            {
                c[ix] = c[--nr_of_cubes];
                
            }
        }
    }
    
    public void init_planes()
    {
        pl = new Plane[3];
        
        Vertex [] v4 = new Vertex[4];
        double x = 0;
        double y = 0;
        double z = 0;
        
        pl_x_offset = 500;
        pl_y_offset = 300;
        
        v4[0] = new Vertex(-plane_w + pl_x_offset , pl_y_offset           ,0);
        v4[1] = new Vertex(-plane_w + pl_x_offset , -plane_w + pl_y_offset,0);
        v4[2] = new Vertex(-plane_w + pl_x_offset , -plane_w + pl_y_offset,-plane_w);
        v4[3] = new Vertex(-plane_w + pl_x_offset , pl_y_offset           ,-plane_w);
        
        x = -plane_w + pl_x_offset;
        y = (pl_y_offset + (-plane_w + pl_y_offset))/2;
        z = -plane_w/2;
        pl[0] = new Plane(v4,plane_rotation,x,y,z);
        
        v4[0] = new Vertex(pl_x_offset           ,pl_y_offset         ,-plane_w);
        v4[1] = new Vertex(-plane_w + pl_x_offset,pl_y_offset         ,-plane_w);
        v4[2] = new Vertex(-plane_w + pl_x_offset,-plane_w+pl_y_offset,-plane_w);
        v4[3] = new Vertex(pl_x_offset           ,-plane_w+pl_y_offset,-plane_w);
        
        x = (pl_x_offset +(-plane_w+pl_x_offset))/2;
        y = (pl_y_offset +(-plane_w+pl_y_offset))/2;
        z = -plane_w;
        
        pl[1] = new Plane(v4,plane_rotation,x,y,z);
        
        v4[0] = new Vertex(-plane_w + pl_x_offset, pl_y_offset, -plane_w);
        v4[1] = new Vertex(-plane_w + pl_x_offset, pl_y_offset, 0);
        v4[2] = new Vertex(pl_x_offset           , pl_y_offset, 0);
        v4[3] = new Vertex(pl_x_offset           , pl_y_offset, -plane_w);
        
        
        x = (pl_x_offset +(-plane_w+pl_x_offset))/2;
        y = pl_y_offset;
        z = -plane_w/2;
        
        pl[2] = new Plane(v4,plane_rotation,x,y,z);

    }
    
    public void init_temple()
    {
        double w = 30;
        double z0 = -500;
        double x0 = -200;
        double y0 = 300;
        
        int z_ = 0;
        int z1_ = 5;
        int x_ = 0;
        int x1_ = 5;
        
        for(int y = 0; y < 3; y++)
        {
            for(int z = z_ ; z < z1_; z++)
            {
                for(int x = x_; x < x1_; x++)
                {
                    double z_cord = z0 + z*w;
                    double x_cord = x0 + x*w;
                    double y_cord = y0 - y*w;
                    add_cube(x_cord,y_cord,z_cord,w,false);
                }
            }
            z_++;
            z1_--;
            x_++;
            x1_--;
        }
       
    }
    
    public void init_surface()
    {
        double w = 30;
        double size = 5;
        
        double z0 = -400;
        double x0 = -400 -(w*size)/2;
        double y0 = 200;
        
        for(int z = 0; z < size; z++)
        {
            for(int x = 0; x < size; x++)
            {
                
                int y_size = 1;
                if(z == 0 || x == 0)
                {
                    y_size = 5;
                }
                for(int y = 0; y < y_size; y++)
                {
                    double z_cord = z0 + z*w;
                    double x_cord = x0 + x*w;
                    double y_cord = y0 - y*w;
                    add_cube(x_cord,y_cord,z_cord,w,false);
                    
                }
            }
        }
        
    }
    
    public void init_rand_cubes()
    {
        for(int ix = 0; ix < 15; ix++)
        {
            double x = -250+rand.nextInt(501);
            double y = -250+rand.nextInt(501);
            double z = -500+rand.nextInt(601);
            double w = 40+rand.nextInt(100);
            int r = rand.nextInt(2);
            boolean rot;
            if(r == 0)
            {
                rot = false;
            }
            else
            {
                rot = true;
            }
            if(!add_cube_rotation)
            {
                rot = false;
            }
            add_cube(x,y,z,w,rot);
        }
    }
    
    public void init_cubes()
    {
        nr_of_cubes = 0;
        cubes_size = 30;
        c = new Cube[cubes_size];
        
        if(init_rand_cubes)
        {
            init_rand_cubes();
        }
        if(init_cube_wall)
        {
            init_surface();
        }
        if(init_temple_wall)
        {
            init_temple();
        }
        
        //z- away from camera
        //z+ towards camera
    }
    
    public void init_light()
    {
        light = new Vec3D(-200,500,-500);
    }
    
    public void init_camera()
    {
        
        cam = new Camera();
        right = new Vec3D(1,0,0);
        up = new Vec3D(0,1,0);
        forward = new Vec3D(0,0,1);
        pos = new Vec3D(0,0,500);
        update_cam_vectors();
        
    }
    
    public void act_perf(int delay)
    {
        ActionListener taskPerformer = new ActionListener()
                {
                    
                    public void actionPerformed(ActionEvent evt)
                    {
                        draw_background();
                        cam_movement();
                        check_shoot_cube_hit();
                        update_jump();
                        move_cubes_orthogonal();
                        
                        for(int ix = 0; ix < 3; ix++)
                        {
                            draw_plane(ix);
                            plane_rotation(ix);
                        }
                        for(int ix = 0; ix < nr_of_bullets; ix++)
                        {
                            b[ix].update();
                            draw_bullets(ix);
                            check_bullet_out(ix);
                        }
                        
                        for(int ix = 0; ix < nr_of_cubes; ix++)
                        {
                            cubes_rotation(ix);
                            draw(ix);
                           
                        }
                        
                    }
                };
        
        
        t1 = new Timer(delay,taskPerformer);
        t1.start();
    }
    
    public void init()
    {
        delay = 5;
        FRAME_WIDTH = this.jPanel1.getWidth();
        g = jPanel1.getGraphics();
        rand = new Random();
        
    }
    
    public void plane_rotation(int ix)
    {
        if(pl[ix].is_rotation())
        {
            pl[ix].x_rotate();
            pl[ix].y_rotate();
            pl[ix].z_rotate();
        }
    }
    
    public double eval_plane_light(int pl_ix)
    {
        for(int ix = 0; ix < 3; ix++)
        {
            light = light.normalize();
        }
        
        double dp = pl[pl_ix].get_triangle(0).x_norm_normal() * light.x() + pl[pl_ix].get_triangle(0).y_norm_normal() * light.y() + pl[pl_ix].get_triangle(0).z_norm_normal() * light.z(); 

        Vec3D p0 = pos.normalize();
        Vec3D p1 = light.normalize();
        double dp2 = p0.dot_product(p1);
        
        if(dp > 0 && dp2 < 0)
        {
            dp *= -1;
        }
        if(dp2 > 0)
        {
            dp = 0;
        }
        
        return dp;
    }
    
    
    public boolean is_z_visible_plane(int pl_ix)
    {
        boolean status = false;
        double x_norm = pl[pl_ix].get_triangle(0).x_norm_normal() * (pl[pl_ix].get_triangle(0).get_vertex_x(0) - pos.x());
        double y_norm = pl[pl_ix].get_triangle(0).y_norm_normal() * (pl[pl_ix].get_triangle(0).get_vertex_y(0) - pos.y());
        double z_norm = pl[pl_ix].get_triangle(0).z_norm_normal() * (pl[pl_ix].get_triangle(0).get_vertex_z(0) - pos.z());
        
        double sum = x_norm + y_norm + z_norm;
        if(sum < 0)
        {
            status = true;
        }
        return status;
    }
    
    public boolean is_z_visible(int cubes_ix, int ix)
    {
        boolean status = false;
        double x_norm = c[cubes_ix].get_triangle(ix).x_norm_normal() * (c[cubes_ix].get_triangle(ix).get_vertex_x(2) - pos.x());
        double y_norm = c[cubes_ix].get_triangle(ix).y_norm_normal() * (c[cubes_ix].get_triangle(ix).get_vertex_y(2) - pos.y());
        double z_norm = c[cubes_ix].get_triangle(ix).z_norm_normal() * (c[cubes_ix].get_triangle(ix).get_vertex_z(2) - pos.z());
        
        double sum = x_norm + y_norm + z_norm;
        if(sum < 0)
        {
            status = true;
        }
        
        return status;
    }
    
    
    public double eval_light(int cubes_ix, int tri)
    {
     
        for(int ix = 0; ix < 3; ix++)
        {
            light = light.normalize();
            
        }
        double dp = c[cubes_ix].get_triangle(tri).x_norm_normal() * light.x() + c[cubes_ix].get_triangle(tri).y_norm_normal() * light.y() + c[cubes_ix].get_triangle(tri).z_norm_normal() * light.z();
        return dp;
    }
    
    
    public void sort_cubes_dist()
    {
        boolean swap;
        do
        {
            swap = false;
            for(int ix = 0; ix < nr_of_cubes - 1; ix++)
            {
                Vec3D d = new Vec3D();
                Vec3D c0 = new Vec3D(c[ix].get_cube_cent_x(),c[ix].get_cube_cent_y(),c[ix].get_cube_cent_z());
                Vec3D c1 = new Vec3D(c[ix+1].get_cube_cent_x(),c[ix+1].get_cube_cent_y(),c[ix+1].get_cube_cent_z());
                double d0 = d.dist(c0,pos);
                double d1 = d.dist(c1,pos);
                if(d0 < d1)
                {
                    Cube temp = c[ix];
                    c[ix] = c[ix+1];
                    c[ix+1] = temp;
                    swap = true;
                }
            }
        }
        while(swap);
    }
    
    public void set_glob_rotation()
    {
        Vec3D dist = new Vec3D();
        Vec3D front_pos = pos.add(forward.scale(200));
        
        for(int ix = 0; ix < nr_of_cubes; ix++)
        {
            Vec3D cube = new Vec3D(c[ix].get_cube_cent_x(),c[ix].get_cube_cent_y(),c[ix].get_cube_cent_z());
            double d = dist.dist(front_pos, cube);
            if(d < 450)
            {
                c[ix].init_global_rotation(front_pos);
            }
        }
    }
    
    public boolean is_plane_front(Plane p)
    {
        boolean is_front = false;
        Vec3D front = pos.add(forward.scale(10));
        Vec3D back = pos.subtract(forward.scale(10));
        Vec3D plane_vec = new Vec3D(p.get_x(),p.get_y(),p.get_z());
        
        Vec3D d = new Vec3D();
        
        double d0 = d.dist(front,plane_vec);
        double d1 = d.dist(back,plane_vec);
        if(d0 < d1)
        {
            is_front = true;
        }
        
        return is_front;
    }
    
    public boolean is_cube_front(Cube cube)
    {
        boolean is_front = false;
        Vec3D front = pos.add(forward.scale(10));
        Vec3D back = pos.subtract(forward.scale(10));
        Vec3D cube_vec = new Vec3D(cube.get_cube_cent_x(),cube.get_cube_cent_y(),cube.get_cube_cent_z());
        
        Vec3D d = new Vec3D();
        
        double d0 = d.dist(front,cube_vec);
        double d1 = d.dist(back,cube_vec);
        if(d0 < d1)
        {
            is_front = true;
        }
        
        return is_front;
    }
    
    
    public void global_rotation_resolve()
    {
                
        if (is_global_rotation) 
        {
            set_glob_rotation();
        } 
        else if (!is_global_rotation) 
        {
           
            for (int ix = 0; ix < nr_of_cubes; ix++) 
            {
                boolean rotate = false;
                if (c[ix].x_angle() != 0 || c[ix].y_angle() != 0 || c[ix].z_angle() != 0) 
                {
                    rotate = true;
                }
                c[ix].set_rot_angles_zero();
                c[ix].set_glob_cords_zero();
                if(rotate)
                {
                    double w = c[ix].get_width();
                    double x0 = c[ix].get_Vertex_x(0, 0);
                    double y0 = c[ix].get_Vertex_y(0, 0) - w / 2;
                    double z0 = c[ix].get_Vertex_z(0, 0) + w / 2;
                    c[ix] = new Cube(x0,y0,z0,w,rotate);
                }
                else
                {
                    if(add_cube_orthogonal)
                    {
                        double w = c[ix].get_width();
                        double x0 = c[ix].get_Vertex_x(0, 0);
                        double y0 = c[ix].get_Vertex_y(0, 0) - w / 2;
                        double z0 = c[ix].get_Vertex_z(0, 0) + w / 2;
                        c[ix] = new Cube(x0,y0,z0,w,rotate);
                    }
                }

            }
            
            
        }
    }
    
    public void bubble_sort(Cube cube)
    {
        
        Triangle [] tri = cube.get_triangles();
        boolean swap;
        do
        {
            swap = false;
            for(int ix = 0; ix < 11; ix++)
            {
                double z_val = 0;
                for(int z = 0; z < 3; z++)
                {
                    z_val += tri[ix].get_vertex_z(z);
                }
                z_val /= 3;
                double z2_val = 0;
                for(int z = 0; z < 3; z++)
                {
                    z2_val += tri[ix+1].get_vertex_z(z);
                }
                z2_val /= 3; 
                
                if(z_val > z2_val)
                {
                    Triangle temp = tri[ix];
                    tri[ix] = tri[ix+1];
                    tri[ix+1] = temp;
                    swap = true;
                }
                
            }
            
        }
        while(swap);
        cube.set_triangles(tri);
    }
    
    public void cam_movement()
    {
        if(left_pressed) //left
        {
            pos = pos.subtract(right.scale(10));
        }
        if(up_pressed) // up
        {
            pos = pos.subtract(up.scale(10));
        }
        if(right_pressed) //right
        {
            pos = pos.add(right.scale(10));
        }
        if(down_pressed) //down
        {
            pos = pos.add(up.scale(10));
        }
        if(left_pressed) //a
        {
            pos = pos.subtract(right.scale(10));
        }
        if(right_pressed) //d
        {
            pos = pos.add(right.scale(10)); 
        }
        if(forw_pressed) //w
        {
            pos = pos.add(forward.scale(10));
        }
        if(back_pressed) //s
        {
            pos = pos.subtract(forward.scale(10));
        }
        
    }
    
    public double [] draw_cords(Vertex v)
    {
        double [] row = new double[4];
        row[0] = v.get_x();
        row[1]=  v.get_y();
        row[2] = v.get_z();
        row[3] = 1;
        double [] sum = new double[4];
        double [][] view_matrix = cam.LookAt(right, up, forward ,pos);
        
        for(int x = 0; x < 4; x++)
        {
            sum[x] = 0;
            for(int y = 0; y < 4; y++)
            {
                sum[x] += row[y] * view_matrix[x][y];
            }
        }
        return sum;
    }
    
    public void draw_background()
    {
        g.setColor(Color.black); 
        g.fillRect(0,0,FRAME_WIDTH,FRAME_WIDTH);
    }
    
    public void check_bullet_out(int ix)
    {
        Vec3D v = new Vec3D();
        double dist = v.dist(b[ix].pos, pos);
        double rad = 1000 * (1.0 / dist);
        if(rad < 0.25)
        {
            b[ix] = b[--nr_of_bullets];
        }
    }
    
    public void check_shoot_cube_hit()
    {
        Vec3D d = new Vec3D();
        for(int b0 = 0; b0 < nr_of_bullets; b0++)
        {
            for(int c0 = 0; c0 < nr_of_cubes; c0++)
            {
                Vec3D cube_pos = new Vec3D(c[c0].get_cube_cent_x(),c[c0].get_cube_cent_y(),c[c0].get_cube_cent_z());         
                double dist = d.dist(b[b0].pos, cube_pos);
                if(dist < c[c0].get_width())
                {
                    if(nr_of_cubes > 0)
                    {
                        c[c0] = c[--nr_of_cubes];
                    }
                    if(nr_of_bullets > 0)
                    {
                        b[b0] = b[--nr_of_bullets];
                    }
                   
                }
            }
        }
    }
    
    public void move_cubes_orthogonal()
    {
        if(move_cubes_orth)
        {
            orth_counter++;
            if(orth_counter % orth_modulo == 0)
            {
                for(int ix = 0; ix < nr_of_cubes; ix++)
                {
                    c[ix].move_orthogonal();
                }
            }
        }
    }
    
    public void draw_bullets(int ix)
    {
        
        
        g.setColor(Color.blue);
        Vertex v1 = new Vertex(b[ix].get_x(),b[ix].get_y(),b[ix].get_z());
        
        double [] cords1 = draw_cords(v1);
            
        
                    cords1[0] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    cords1[1] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    
                    
                    cords1[0] *= 2500;
                    cords1[1] *= 2500;
                    
                    
                    cords1[0] *= 1.0 / Math.tan(pr_angle/2);
                    cords1[1] *= 1.0 / Math.tan(pr_angle/2);
                    
                    
                    cords1[0] /= cords1[2];
                    cords1[1] /= cords1[2];
                    
                    Vec3D v = new Vec3D();
                    double dist = v.dist(b[ix].pos, pos);
                    
                    double rad = 5000 * (1.0 / dist);
                    
                    
                    cords1[0] *= 100.0;
                    cords1[1] *= 100.0;
                    
                    
                    cords1[0] += FRAME_WIDTH/2;
                    cords1[1] += FRAME_WIDTH/2;
        
                    g.fillOval((int)(cords1[0]-rad),(int)(cords1[1]-rad),(int)(rad*2),(int)(rad*2));
        
                    
        
    }
    
    public void draw_plane(int ix)
    {
                double [] x_points = new double [4];
                double [] y_points = new double [4];
                
                
                
                for(int x = 0; x < 4; x++)
                {
                    
                    double [] cords1 = draw_cords(pl[ix].get_vertex(x));
                    
                    
                    cords1[0] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    cords1[1] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    
                    
                    cords1[0] *= 2500;
                    cords1[1] *= 2500;
                    
                    
                    cords1[0] *= 1.0 / Math.tan(pr_angle/2);
                    cords1[1] *= 1.0 / Math.tan(pr_angle/2);
                    
                    
                    cords1[0] /= cords1[2];
                    cords1[1] /= cords1[2];
                    
                    
                    
                    cords1[0] *= 100.0;
                    cords1[1] *= 100.0;
                    
                    
                    cords1[0] += FRAME_WIDTH/2;
                    cords1[1] += FRAME_WIDTH/2;
                    
                    x_points[x] = (int)cords1[0];
                    y_points[x] = (int)cords1[1];
                    
                }
                
            
                int col = (int)(eval_plane_light(ix)*355);  
                
                col *= -1;
                if(col < 0)
                {
                    col = 0;
                }
                if(col > 255)
                {
                    col = 255;
                }
                
                if(col < 80)
                {
                    col = 80;
                }
                
                if(fill_transparency)
                {
                    g.setColor(new Color(col,col,col,80));
                }
                else
                {
                    g.setColor(new Color(col,col,col));
                }
                
                
                
                
                int [] x_p = new int[4];
                int [] y_p = new int[4];
                for(int z = 0; z < 4; z++)
                {
                    x_p[z] = (int)x_points[z];
                    y_p[z] = (int)y_points[z];
                }
                
                boolean draw = true;
                if(ix == 0 && !jRadioButton1.isSelected())
                {
                    draw = false;
                }
                else if(ix == 1 && !jRadioButton2.isSelected())
                {
                    draw = false;
                }
                else if(ix == 2 && !jRadioButton3.isSelected())
                {
                    draw = false;
                }
                
                boolean within = true;
                for(int k = 0; k < 4; k++)
                {
                    if(x_p[k] < -1500 || x_p[k] > jPanel1.getWidth() + 1500)
                    {
                        within = false;
                    }
                    if(y_p[k] < -1500 || y_p[k] > jPanel1.getHeight() + 1500)
                    {
                        within = false;
                    }
                }
                
                
               
                if(draw && is_plane_front(pl[ix]) && within)
                {
                    
                    g.fillPolygon(x_p,y_p,4);
                }
                
    }
   
    public void draw(int cubes_ix)
    {
       
        bubble_sort(c[cubes_ix]);
       
        for(int ix = 0; ix < 12; ix++)
        {
                
                double [] x_points = new double [4];
                double [] y_points = new double [4];
                
                for(int x = 0; x < 4; x++)
                {
                    int k = x % 3;
                    
                    double [] cords1 = draw_cords(c[cubes_ix].get_Vertex(ix, k));
                    
                    
                    cords1[0] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    cords1[1] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    
                    
                    cords1[0] *= 2500;
                    cords1[1] *= 2500;
                    
                    
                    cords1[0] *= 1.0 / Math.tan(pr_angle/2);
                    cords1[1] *= 1.0 / Math.tan(pr_angle/2);
                    
                    
                    cords1[0] /= cords1[2];
                    cords1[1] /= cords1[2];
                    
                    
                    
                    cords1[0] *= 100.0;
                    cords1[1] *= 100.0;
                    
                    
                    cords1[0] += FRAME_WIDTH/2;
                    cords1[1] += FRAME_WIDTH/2;
                    
                    x_points[x] = (int)cords1[0];
                    y_points[x] = (int)cords1[1];
                    
                   
                }
                
            
                int col = (int)(eval_light(cubes_ix,ix)*355);  
                col *= -1;
                if(col < 0)
                {
                    col = 0;
                }
                if(col > 255)
                {
                    col = 255;
                }
                
                if(col < 80)
                {
                    col = 80;
                }
                
                
                if(fill_transparency)
                {
                    g.setColor(new Color(col,col,col,80));
                }
                else
                {
                    g.setColor(new Color(col,col,col));
                }
                
                int [] x_p = new int[4];
                int [] y_p = new int[4];
                for(int z = 0; z < 4; z++)
                {
                    x_p[z] = (int)x_points[z];
                    y_p[z] = (int)y_points[z];
                }
                
                double within_dist = 300;
                boolean within = true;
                for(int k = 0; k < 4; k++)
                {
                    if(x_p[k] < -within_dist || x_p[k] > jPanel1.getWidth()+within_dist)
                    {
                        within = false;
                    }
                    if(y_p[k] < -within_dist || y_p[k] > jPanel1.getHeight()+within_dist)
                    {
                        within = false;
                    }
                }
                
                if(is_z_visible(cubes_ix,ix) && is_cube_front(c[cubes_ix]) && within)
                {
                   g.fillPolygon(x_p,y_p,4);
                }
                g.setColor(Color.black);
                
                
                for(int k  = 0; k < 3; k++)
                {
                    int k2 = k + 1;
                    k2 = k2 % 3;
                    double [] cords1 = draw_cords(c[cubes_ix].get_Vertex(ix, k));
                    double [] cords2 = draw_cords(c[cubes_ix].get_Vertex(ix, k2));
                    
                    
                    
                    cords1[0] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    cords1[1] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k));
                    cords2[0] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k2));
                    cords2[1] *= 1.0 / (z_far - z_near);//c.get_Vertex_z(ix,k2));
                    
                    
                    cords1[0] *= 2500;
                    cords1[1] *= 2500;
                    cords2[0] *= 2500;
                    cords2[1] *= 2500;
                    
                    
                    cords1[0] *= 1.0 / Math.tan(pr_angle/2);
                    cords1[1] *= 1.0 / Math.tan(pr_angle/2);
                    cords2[0] *= 1.0 / Math.tan(pr_angle/2);
                    cords2[1] *= 1.0 / Math.tan(pr_angle/2);
                    
                    
                    
                    cords1[0] /= cords1[2];
                    cords1[1] /= cords1[2];
                    cords2[0] /= cords2[2];
                    cords2[1] /= cords2[2];
                    
                    
                    
                    cords1[0] *= 100.0;
                    cords1[1] *= 100.0;
                    cords2[0] *= 100.0;
                    cords2[1] *= 100.0;
                    
                    
                    cords1[0] += FRAME_WIDTH/2;
                    cords1[1] += FRAME_WIDTH/2;
                    cords2[0] += FRAME_WIDTH/2;
                    cords2[1] += FRAME_WIDTH/2;
                    
                    
                    
                    g.setColor(Color.white);
                    if(is_z_visible(cubes_ix,ix) && draw_lines && within)
                    {
                        g.drawLine((int)cords1[0],(int)cords1[1],(int)cords2[0],(int)cords2[1]);
                    }
                    
                
                }
                
        }
       
    }
    
    public void update_cam_vectors()
    {
        if(cam_movement)
        {
            /*
            if(pitch >= Math.PI/2)
            {
                yaw += Math.PI;
                pitch -= Math.PI/16;
            }
            else if(pitch <= -Math.PI/2)
            {
                yaw += Math.PI;
                pitch += Math.PI/16;
            }
            */
            
            if(pitch >= Math.PI/2)
            {
                pitch -= Math.PI/128;
            }
            else if(pitch <= -Math.PI/2)
            {
                pitch += Math.PI/128;
            }
            
            double front_x = Math.cos(yaw) * Math.cos(pitch);
            double front_y = Math.sin(pitch);
            double front_z = Math.sin(yaw) * Math.cos(pitch);
        
        
            Vec3D fr = new Vec3D(front_x,front_y,front_z);
            fr = fr.normalize();
            Vec3D r = new Vec3D();
            Vec3D up_vec = new Vec3D(0,1,0);
            r = r.cross_product(fr, up_vec);
            r = r.normalize();
            Vec3D u = new Vec3D();
            u = u.cross_product(r, fr);
            u = u.normalize();
        
            this.right = r;
            this.forward = fr;
            this.up = u;
            
            
            
        }
        
    }
    
    public void cubes_rotation(int ix)
    {
        c[ix].x_rotate();
        c[ix].y_rotate();
        c[ix].z_rotate();

        if(is_global_rotation)
        {
            c[ix].global_x_rotate();
            c[ix].global_y_rotate();
            c[ix].global_z_rotate();
        }
    }
    
        
    public void set_pressed_false()
    {
        left_pressed = false;
        right_pressed = false;
        up_pressed = false;
        down_pressed = false;
        forw_pressed = false;
        back_pressed = false;
    }
    

    /**
     * This method is called from within theq constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jSlider3 = new javax.swing.JSlider();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 600));
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel1MouseMoved(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jCheckBox1.setText("add cube rotation");
        jCheckBox1.setFocusable(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("init cube wall");
        jCheckBox2.setFocusable(false);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("init rand cubes");
        jCheckBox3.setFocusable(false);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("draw lines");
        jCheckBox4.setFocusable(false);
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox6.setText("plane rotation");
        jCheckBox6.setFocusable(false);
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("x plane");
        jRadioButton1.setFocusable(false);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("y plane");
        jRadioButton2.setFocusable(false);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jRadioButton3.setSelected(true);
        jRadioButton3.setText("z plane");
        jRadioButton3.setFocusable(false);
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Calibri", 0, 20)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("3d landscape how to:\n\narrows, a, s, d, w         move camera\nmouse movement       move camera\nmouse scroll\t            zoom in/zoom out\nmouse click \t            shoot bullet\nspace\tadd cube\nenter\tinit landscape/planes\nq\tno mouse cam movement\nc\tinit camera\nl\tset light\nr\trotate around point\ne\tjump\nb\tmove cubes auto orthogonal\nx\tmove around camera (PI)\nv\tset cubes rotation\nshift\tremove cubes");
        jTextArea1.setCaretPosition(0);
        jTextArea1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextArea1.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea1);

        jCheckBox5.setText("init temple wall");
        jCheckBox5.setFocusable(false);
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("plane width");

        jSlider1.setMaximum(700);
        jSlider1.setMinimum(50);
        jSlider1.setFocusable(false);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("init cube orthogonal");
        jCheckBox7.setFocusable(false);
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        jCheckBox8.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jCheckBox8.setSelected(true);
        jCheckBox8.setText("3d cam perspective effect");
        jCheckBox8.setFocusable(false);
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("add cube width");
        jLabel2.setFocusable(false);

        jSlider2.setMaximum(300);
        jSlider2.setMinimum(20);
        jSlider2.setFocusable(false);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("mouse sensivity 1.0");
        jLabel3.setFocusable(false);

        jSlider3.setMaximum(17);
        jSlider3.setMinimum(8);
        jSlider3.setFocusable(false);
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });

        jCheckBox9.setSelected(true);
        jCheckBox9.setText("add cube grid");
        jCheckBox9.setFocusable(false);
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox9ActionPerformed(evt);
            }
        });

        jCheckBox10.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jCheckBox10.setText("color fill transparency");
        jCheckBox10.setFocusable(false);
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox7)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox2)
                                    .addComponent(jCheckBox5)
                                    .addComponent(jCheckBox3)))
                            .addComponent(jCheckBox4)
                            .addComponent(jCheckBox9))
                        .addGap(79, 79, 79)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jRadioButton1)
                                            .addGap(59, 59, 59)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jRadioButton3)
                                                .addComponent(jRadioButton2))
                                            .addGap(59, 59, 59)
                                            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(2, 2, 2)
                                            .addComponent(jCheckBox6))
                                        .addComponent(jCheckBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox1)
                            .addComponent(jLabel2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox9)
                        .addGap(23, 23, 23)
                        .addComponent(jCheckBox10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton2))
                            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(69, 69, 69))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

        
        if(evt.getKeyCode() == 81)
        {
            cam_movement = false;
            set_pressed_false();
        }
        
        
        if(cam_movement)
        {
           
            //System.out.println(evt.getKeyCode());
            if(evt.getKeyCode() == 32)
            {
                Vec3D cube_cords = new Vec3D();
                cube_cords = pos.add(forward.scale(300));
                double x = cube_cords.x();
                double y = cube_cords.y();
                double z = cube_cords.z();
                double w = add_cube_width;
                add_cube(x,y,z,w,add_cube_rotation);
            
                // z -> right
                // y -> forward
                // x -> up
            
                
                
            }
            else if(evt.getKeyCode() == 16)
            {
                Vec3D cube_cords = new Vec3D();
                cube_cords = pos.add(forward.scale(300));
                remove_cubes(cube_cords);
            }
            else if(evt.getKeyCode() == 86)
            {
                Vec3D cube_cords = new Vec3D();
                cube_cords = pos.add(forward.scale(300));
                rotate_cubes(cube_cords);
            }
            else if(evt.getKeyCode() == 10)
            {
                init_cubes();
                init_planes();
            }
            else if(evt.getKeyCode() == 37) //left
            {
                left_pressed = true;
            }
            else if(evt.getKeyCode() == 38) // up
            {
                up_pressed = true;
            }
            else if(evt.getKeyCode() == 39) //right
            {
                right_pressed = true;
            }
            else if(evt.getKeyCode() == 40) //down
            {
                down_pressed = true;
            }
            else if(evt.getKeyCode() == 65) //a
            {
                left_pressed = true;
            }
            else if(evt.getKeyCode() == 68) //d
            {
                right_pressed = true;
            }
            else if(evt.getKeyCode() == 87) //w
            {
                forw_pressed = true;
            }
            else if(evt.getKeyCode() == 83) //s
            {
                back_pressed = true;
            }
            else if(evt.getKeyCode() == 67)
            {
                right = new Vec3D(1,0,0);
                up = new Vec3D(0,1,0);
                forward = new Vec3D(0,0,1);
                pos = new Vec3D(0,0,1000);
                yaw = -Math.PI/2;
                pitch = 0;
                mouse_sens = 1.0;
                update_cam_vectors();
            
            }
            else if(evt.getKeyCode() == 81)
            {
                cam_movement = false;
            }
            else if(evt.getKeyCode() == 76)
            {
                light = pos.scale(-1);
            }
            else if(evt.getKeyCode() == 82)
            {
                /*
                if(!move_cubes_orth)
                {
                    is_global_rotation = !is_global_rotation;
                    global_rotation_resolve();
                }
                */
                
                is_global_rotation = !is_global_rotation;
                global_rotation_resolve();
            }
            else if(evt.getKeyCode() == 69)
            {
                jump();
            }
            else if(evt.getKeyCode() == 66)
            {
                /*
                if(!is_global_rotation)
                {
                    move_cubes_orth = !move_cubes_orth;
                }
                */
                move_cubes_orth = !move_cubes_orth;
            }
            else if(evt.getKeyCode() == 88)
            {
                yaw += Math.PI;
                update_cam_vectors();
            }
            sort_cubes_dist();
        }
        
        
        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void jPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseMoved

        if(cam_movement)
        {
            curx = evt.getX();
            cury = evt.getY();
        
        
            if(evt.getX() > lastx)
            {
                yaw += Math.PI/(128*mouse_sens);     //192
            }
            else if(evt.getX() < lastx)
            {
                yaw -= Math.PI/(128*mouse_sens);
            }
        
            if(evt.getY() > lasty)
            {
                pitch += Math.PI/(260*mouse_sens);     //512
            }
            else if(evt.getY() < lasty)
            {
                pitch -= Math.PI/(260*mouse_sens);
            }
        
        
            lastx = curx;
            lasty = cury;
        
            update_cam_vectors();
        }
       
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseMoved

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved


        if(evt.getWheelRotation() == 1)
        {
            pr_angle += Math.PI/128;
        }
        else if(evt.getWheelRotation() == -1)
        {
            pr_angle -= Math.PI/128;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseWheelMoved

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased

        if(evt.getKeyCode() == 81)
        {
            cam_movement = true;
        }
        
        if(cam_movement)
        {
            if(evt.getKeyCode() == 37) //left
            {
                left_pressed = false;
            }
            else if(evt.getKeyCode() == 38) // up
            {
                up_pressed = false;
            }
            else if(evt.getKeyCode() == 39) //right
            {
                right_pressed = false;
            }
            else if(evt.getKeyCode() == 40) //down
            {
                down_pressed = false;
            }
            else if(evt.getKeyCode() == 65) //a
            {
                left_pressed = false;
            }
            else if(evt.getKeyCode() == 68) //d
            {
                right_pressed = false;
            }
            else if(evt.getKeyCode() == 87)
            {
                forw_pressed = false;
            }
            else if(evt.getKeyCode() == 83)
            {
                back_pressed = false;
            }
            else if(evt.getKeyCode() == 81)
            {
                cam_movement = true;
            }
        }
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyReleased

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed

        if(cam_movement)
        {
            bullet_counter++;
            if(bullet_counter % bullet_modulo == 0)
            {
                add_bullet(pos,this.forward,50);
            }
        }
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        
        if(cam_movement)
        {
            bullet_counter++;
            if(bullet_counter % bullet_modulo == 0)
            {
                add_bullet(pos,this.forward,50);
            }
       
            curx = evt.getX();
            cury = evt.getY();
            if(evt.getX() > lastx)
            {
                yaw += Math.PI/(128*mouse_sens);
            }
            else if(evt.getX() < lastx)
            {
                yaw -= Math.PI/(128*mouse_sens);
            }
        
            if(evt.getY() > lasty)
            {   
                pitch += Math.PI/(260*mouse_sens);
            }
            else if(evt.getY() < lasty)
            {
                pitch -= Math.PI/(260*mouse_sens);
            }
        
        
            lastx = curx;
            lasty = cury;
        
            update_cam_vectors();
        }
        
        
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        if(jCheckBox1.isSelected())
        {
            add_cube_rotation = true;
        }
        else
        {
            add_cube_rotation = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed

        if(jCheckBox2.isSelected())
        {
            init_cube_wall = true;
        }
        else
        {
            init_cube_wall = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed

        if(jCheckBox3.isSelected())
        {
            init_rand_cubes = true;
        }
        else
        {
            init_rand_cubes = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed

        if(jCheckBox4.isSelected())
        {
            draw_lines = true;
        }
        else
        {
            draw_lines = false;
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed

        if(jCheckBox6.isSelected())
        {
            plane_rotation = true;
        }
        else
        {
            plane_rotation = false;
        }
        for(int ix = 0; ix < 3; ix++)
        {
            pl[ix].set_rotation(plane_rotation);
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed

        init_planes();
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed

        init_planes();
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed

        init_planes();
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed

        if(jCheckBox5.isSelected())
        {
            init_temple_wall = true;
        }
        else
        { 
            init_temple_wall = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

        plane_w = jSlider1.getValue();
        jLabel1.setText("plane width "+plane_w);
        init_planes();
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider1StateChanged

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed

        if(jCheckBox7.isSelected())
        {
            add_cube_orthogonal = true;
        }
        else
        {
            add_cube_orthogonal = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jCheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8ActionPerformed

        if(jCheckBox8.isSelected())
        {
            z_near = -100.0;
        }
        else
        {
            z_near = 100.0;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged

        add_cube_width = jSlider2.getValue();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider2StateChanged

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged


        
        double val = (jSlider3.getMinimum()+(jSlider3.getMaximum())) - jSlider3.getValue();
        val /= 10;
        mouse_sens = val;
        jLabel3.setText("mouse sensivity "+(double)jSlider3.getValue()/10.0);
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider3StateChanged

    private void jCheckBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox9ActionPerformed

        if(jCheckBox9.isSelected())
        {
            add_grid = true;
        }
        else
        {
            add_grid = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox9ActionPerformed

    private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed

        if(jCheckBox10.isSelected())
        {
            fill_transparency = true;
        }
        else
        {
            fill_transparency = false;
        }
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frame_3d.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame_3d.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame_3d.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame_3d.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new frame_3d().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
