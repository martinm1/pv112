package cz.muni.fi.pv112.cv3;

import com.hackoeur.jglm.Mat3;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import com.hackoeur.jglm.MatricesUtils;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.FBObject;
import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_BACK;
import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_COMPONENT24;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.GL.GL_LINEAR;
import static com.jogamp.opengl.GL.GL_LINEAR_MIPMAP_LINEAR;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_REPEAT;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TEXTURE0;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_T;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL2ES3.GL_VERTEX_ARRAY_BINDING;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import com.jogamp.opengl.GL3;
import static com.jogamp.opengl.GL3.*;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLDebugListener;
import com.jogamp.opengl.GLDebugMessage;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam Jurcik <xjurc@fi.muni.cz>
 */
public class Scene implements GLEventListener {

    private FPSAnimator animator;
    private Camera camera;
    private int mode = GL_FILL;

    // window size
    private int width;
    private int height;
    
    private static final int SIZEOF_QUAD_VERTEX = 2 * Buffers.SIZEOF_FLOAT;
    
    private static final float QUAD[] = {
        // .. position ..
        // first triangle
         1.0f,  1.0f, // 1
        -1.0f,  1.0f, // 2
        -1.0f, -1.0f, // 3
        // second triangle
        -1.0f, -1.0f, // 3
         1.0f, -1.0f, // 4
         1.0f,  1.0f  // 1
    };
    private int quadBuffer;
    private int quadArray;
    
    private int quadProgram;
    
    private int sceneTexLoc;
    private int sceneTexLoc2;
    private int tLoc;
    private FBObject framebuffer;
    private FBObject.TextureAttachment sceneTexAttachment;
    private FBObject framebuffer2;
    private FBObject.TextureAttachment sceneTexAttachment2;
    

    // models
    private Geometry teapotGeometry;
    
    private Geometry wallGeometry; 
    private Geometry bedGeometry; 
    private Geometry tableGeometry; 
    private Geometry chairGeometry;
    private Geometry doorGeometry; 
    private Geometry ceilingGeometry; 
    private Geometry aquariumGeometry; 
    private Geometry fishGeometry; 
    private Geometry sphereGeometry; 
    private Geometry cubeGeometry;
    private Geometry cylinderGeometry; 
    private Geometry doorframeGeometry; 
    private Geometry windowGeometry; 
    private Geometry roofGeometry; 
    private Geometry clockGeometry; 
    private Geometry imageframeGeometry;
    private Geometry imageframe2Geometry; 
    private Geometry flashlightGeometry; 

    // JOGL resouces
    private int joglArray; // JOGL uses own vertex array for updating GLJPanel
    
    private int wallProgram;
    private int wallMvpUniformLoc;
    
    private int bedProgram;
    private int bedMvpUniformLoc;
    
    private int aquariumProgram;
    private int aquariumMvpUniformLoc;
    
    // our GLSL resources (model)
    private int teapotProgram;
    private int modelLoc;
    private int mvpLoc;
    private int nLoc;

    private int materialAmbientColorLoc;
    private int materialDiffuseColorLoc;
    private int materialSpecularColorLoc;
    private int materialShininessLoc;

    private int light1PositionLoc;
    private int light1AmbientColorLoc;
    private int light1DiffuseColorLoc;
    private int light1SpecularColorLoc;
    
    
    private int light2PositionLoc;
    private int light2AmbientColorLoc;
    private int light2DiffuseColorLoc;
    private int light2SpecularColorLoc;
    
    private int light3PositionLoc;
    private int light3AmbientColorLoc;
    private int light3DiffuseColorLoc;
    private int light3SpecularColorLoc;
    
    private int light3DirectionLoc;
    private int light3CutOffLoc;
    
            
    private int teapot_alpha_tex_loc;
    private int teapot_color_tex_loc;
    
    private int wall_alpha_tex_loc;
    private int wall_color_tex_loc;
    private int bed_alpha_tex_loc;
    private int bed_color_tex_loc;
    private int aquarium_alpha_tex_loc;
    private int aquarium_color_tex_loc;

    private int eyePositionLoc;
    private Texture alpha1;
    private Texture alpha2;
    private Texture lenna;
    
    private Texture strop;
    private Texture postel;
    private Texture stul;
    private Texture zidle;
    private Texture dvere;
    private Texture akvarko;
    private Texture rybka;
    private Texture bila;
    private Texture okno;
    private Texture strecha;
    private Texture hodiny;
    private Texture napis;
    private Texture baterka;

    public Scene(FPSAnimator animator, Camera camera) {
        this.animator = animator;
        this.camera = camera;
    }

    public void toggleLines() {
        mode = GL_LINE;
    }

    public void toggleFill() {
        mode = GL_FILL;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();

        // add listener that will inform us when we make an error.
        gl.getContext().addGLDebugListener(new GLDebugListener() {
            @Override
            public void messageSent(GLDebugMessage event) {
                switch (event.getDbgType()) {
                    case GL_DEBUG_TYPE_ERROR:
                    case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
                    case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
                        System.err.println(event.getDbgMsg());
                        break;
                }
            }
        });

        // empty scene color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
       // gl.glClearDepth(1.0);
      //  gl.glLineWidth(3.0f);

        // enable depth test
        gl.glEnable(GL_DEPTH_TEST);

        // load GLSL program (vertex and fragment shaders)
        try {
            quadProgram = loadProgram(gl, "/resources/shaders/quad.vs.glsl",
                    "/resources/shaders/quad.fs.glsl");
            teapotProgram = loadProgram(gl, "/resources/shaders/teapot.vs.glsl",
                    "/resources/shaders/teapot.fs.glsl");
            
            wallProgram = loadProgram(gl, "/resources/shaders/wall.vs.glsl",
                    "/resources/shaders/wall.fs.glsl");
            bedProgram = loadProgram(gl, "/resources/shaders/bed.vs.glsl",
                    "/resources/shaders/bed.fs.glsl");
            aquariumProgram = loadProgram(gl, "/resources/shaders/aquarium.vs.glsl",
                    "/resources/shaders/aquarium.fs.glsl");
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        sceneTexLoc = gl.glGetUniformLocation(quadProgram, "sceneTex");
        sceneTexLoc2 = gl.glGetUniformLocation(quadProgram, "sceneTex2");
        tLoc = gl.glGetUniformLocation(quadProgram, "t");
        
        int[] buffers = new int[2];
	gl.glGenBuffers(2, buffers, 0);
        quadBuffer = buffers[1];
        
        gl.glBindBuffer(GL_ARRAY_BUFFER, quadBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, QUAD.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(QUAD), GL_STATIC_DRAW);
	gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        int[] arrays = new int[2];
	gl.glGenVertexArrays(2, arrays, 0);
        quadArray = arrays[1];
        
        int binding[] = new int[1];
        gl.glGetIntegerv(GL_VERTEX_ARRAY_BINDING, binding, 0);
        joglArray = binding[0];
        
        int positionAttribLoc = gl.glGetAttribLocation(quadProgram, "position");
        // init quad vertex array
        gl.glBindVertexArray(quadArray);
        gl.glBindBuffer(GL_ARRAY_BUFFER, quadBuffer);
        gl.glEnableVertexAttribArray(positionAttribLoc);
        gl.glVertexAttribPointer(positionAttribLoc, 2, GL_FLOAT, false, SIZEOF_QUAD_VERTEX, 0);
        gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        gl.glBindVertexArray(joglArray);
        
        
        wallMvpUniformLoc = gl.glGetUniformLocation(wallProgram, "MVP");
        bedMvpUniformLoc = gl.glGetUniformLocation(bedProgram, "MVP");
        aquariumMvpUniformLoc = gl.glGetUniformLocation(aquariumProgram, "MVP");

        // teapot program uniforms
        modelLoc = gl.glGetUniformLocation(teapotProgram, "model");
        mvpLoc = gl.glGetUniformLocation(teapotProgram, "MVP");
        nLoc = gl.glGetUniformLocation(teapotProgram, "N");

        materialAmbientColorLoc = gl.glGetUniformLocation(teapotProgram, "materialAmbientColor");
        materialDiffuseColorLoc = gl.glGetUniformLocation(teapotProgram, "materialDiffuseColor");
        materialSpecularColorLoc = gl.glGetUniformLocation(teapotProgram, "materialSpecularColor");
        materialShininessLoc = gl.glGetUniformLocation(teapotProgram, "materialShininess");

        light1PositionLoc = gl.glGetUniformLocation(teapotProgram, "light1Position");
        light1AmbientColorLoc = gl.glGetUniformLocation(teapotProgram, "light1AmbientColor");
        light1DiffuseColorLoc = gl.glGetUniformLocation(teapotProgram, "light1DiffuseColor");
        light1SpecularColorLoc = gl.glGetUniformLocation(teapotProgram, "light1SpecularColor");
        
        light2PositionLoc = gl.glGetUniformLocation(teapotProgram, "light2Position");
        light2AmbientColorLoc = gl.glGetUniformLocation(teapotProgram, "light2AmbientColor");
        light2DiffuseColorLoc = gl.glGetUniformLocation(teapotProgram, "light2DiffuseColor");
        light2SpecularColorLoc = gl.glGetUniformLocation(teapotProgram, "light2SpecularColor");
        
        light3PositionLoc = gl.glGetUniformLocation(teapotProgram, "light3Position");
        light3AmbientColorLoc = gl.glGetUniformLocation(teapotProgram, "light3AmbientColor");
        light3DiffuseColorLoc = gl.glGetUniformLocation(teapotProgram, "light3DiffuseColor");
        light3SpecularColorLoc = gl.glGetUniformLocation(teapotProgram, "light3SpecularColor");
        light3DirectionLoc = gl.glGetUniformLocation(teapotProgram, "light3Direction");
        light3CutOffLoc = gl.glGetUniformLocation(teapotProgram, "light3CutOff");
        
        
        eyePositionLoc = gl.glGetUniformLocation(teapotProgram, "eyePosition");

        teapot_alpha_tex_loc = gl.glGetUniformLocation(teapotProgram, "my_alpha_tex");
        teapot_color_tex_loc = gl.glGetUniformLocation(teapotProgram, "my_color_tex");
        
        
        modelLoc = gl.glGetUniformLocation(wallProgram, "model");
        mvpLoc = gl.glGetUniformLocation(wallProgram, "MVP");
        nLoc = gl.glGetUniformLocation(wallProgram, "N");
        materialAmbientColorLoc = gl.glGetUniformLocation(wallProgram, "materialAmbientColor");
        materialDiffuseColorLoc = gl.glGetUniformLocation(wallProgram, "materialDiffuseColor");
        materialSpecularColorLoc = gl.glGetUniformLocation(wallProgram, "materialSpecularColor");
        materialShininessLoc = gl.glGetUniformLocation(wallProgram, "materialShininess");
        light1PositionLoc = gl.glGetUniformLocation(wallProgram, "light1Position");
        light1AmbientColorLoc = gl.glGetUniformLocation(wallProgram, "light1AmbientColor");
        light1DiffuseColorLoc = gl.glGetUniformLocation(wallProgram, "light1DiffuseColor");
        light1SpecularColorLoc = gl.glGetUniformLocation(wallProgram, "light1SpecularColor");
        
        light2PositionLoc = gl.glGetUniformLocation(wallProgram, "light2Position");
        light2AmbientColorLoc = gl.glGetUniformLocation(wallProgram, "light2AmbientColor");
        light2DiffuseColorLoc = gl.glGetUniformLocation(wallProgram, "light2DiffuseColor");
        light2SpecularColorLoc = gl.glGetUniformLocation(wallProgram, "light2SpecularColor");
        eyePositionLoc = gl.glGetUniformLocation(wallProgram, "eyePosition");
        wall_alpha_tex_loc = gl.glGetUniformLocation(wallProgram, "my_alpha_tex");
        wall_color_tex_loc = gl.glGetUniformLocation(wallProgram, "my_color_tex");
        
        
        modelLoc = gl.glGetUniformLocation(bedProgram, "model");
        mvpLoc = gl.glGetUniformLocation(bedProgram, "MVP");
        nLoc = gl.glGetUniformLocation(bedProgram, "N");
        materialAmbientColorLoc = gl.glGetUniformLocation(bedProgram, "materialAmbientColor");
        materialDiffuseColorLoc = gl.glGetUniformLocation(bedProgram, "materialDiffuseColor");
        materialSpecularColorLoc = gl.glGetUniformLocation(bedProgram, "materialSpecularColor");
        materialShininessLoc = gl.glGetUniformLocation(bedProgram, "materialShininess");
        light1PositionLoc = gl.glGetUniformLocation(bedProgram, "light1Position");
        light1AmbientColorLoc = gl.glGetUniformLocation(bedProgram, "light1AmbientColor");
        light1DiffuseColorLoc = gl.glGetUniformLocation(bedProgram, "light1DiffuseColor");
        light1SpecularColorLoc = gl.glGetUniformLocation(bedProgram, "light1SpecularColor");
        
        light2PositionLoc = gl.glGetUniformLocation(bedProgram, "light2Position");
        light2AmbientColorLoc = gl.glGetUniformLocation(bedProgram, "light2AmbientColor");
        light2DiffuseColorLoc = gl.glGetUniformLocation(bedProgram, "light2DiffuseColor");
        light2SpecularColorLoc = gl.glGetUniformLocation(bedProgram, "light2SpecularColor");
        eyePositionLoc = gl.glGetUniformLocation(bedProgram, "eyePosition");
        bed_alpha_tex_loc = gl.glGetUniformLocation(bedProgram, "my_alpha_tex");
        bed_color_tex_loc = gl.glGetUniformLocation(bedProgram, "my_color_tex");
        
        
        modelLoc = gl.glGetUniformLocation(aquariumProgram, "model");
        mvpLoc = gl.glGetUniformLocation(aquariumProgram, "MVP");
        nLoc = gl.glGetUniformLocation(aquariumProgram, "N");
        materialAmbientColorLoc = gl.glGetUniformLocation(aquariumProgram, "materialAmbientColor");
        materialDiffuseColorLoc = gl.glGetUniformLocation(aquariumProgram, "materialDiffuseColor");
        materialSpecularColorLoc = gl.glGetUniformLocation(aquariumProgram, "materialSpecularColor");
        materialShininessLoc = gl.glGetUniformLocation(aquariumProgram, "materialShininess");
        light1PositionLoc = gl.glGetUniformLocation(aquariumProgram, "light1Position");
        light1AmbientColorLoc = gl.glGetUniformLocation(aquariumProgram, "light1AmbientColor");
        light1DiffuseColorLoc = gl.glGetUniformLocation(aquariumProgram, "light1DiffuseColor");
        light1SpecularColorLoc = gl.glGetUniformLocation(aquariumProgram, "light1SpecularColor");
        
        light2PositionLoc = gl.glGetUniformLocation(aquariumProgram, "light2Position");
        light2AmbientColorLoc = gl.glGetUniformLocation(aquariumProgram, "light2AmbientColor");
        light2DiffuseColorLoc = gl.glGetUniformLocation(aquariumProgram, "light2DiffuseColor");
        light2SpecularColorLoc = gl.glGetUniformLocation(aquariumProgram, "light2SpecularColor");
        eyePositionLoc = gl.glGetUniformLocation(aquariumProgram, "eyePosition");
        aquarium_alpha_tex_loc = gl.glGetUniformLocation(aquariumProgram, "my_alpha_tex");
        aquarium_color_tex_loc = gl.glGetUniformLocation(aquariumProgram, "my_color_tex");
        
        
        
        ObjLoader teapot_model = new ObjLoader("/resources/models/teapot-high.obj");
        try {
            teapot_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        // get cube program attributes
        /*int*/ positionAttribLoc = gl.glGetAttribLocation(teapotProgram,"position");
        int normalAttribLoc = gl.glGetAttribLocation(teapotProgram, "normal");
        int textureAttribLoc = gl.glGetAttribLocation(teapotProgram, "tex_coord");
         

        // create geometry
        this.teapotGeometry = Geometry.create(gl, teapot_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);

        
        ObjLoader wall_model = new ObjLoader("/resources/models/ctverecek.obj");
        try {
            wall_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        positionAttribLoc = gl.glGetAttribLocation(wallProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(wallProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(wallProgram, "tex_coord");
        this.wallGeometry = Geometry.create(gl, wall_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        
        
        ObjLoader bed_model = new ObjLoader("/resources/models/Bed.obj");
        try {
            bed_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.bedGeometry = Geometry.create(gl, bed_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        
        ObjLoader ceiling_model = new ObjLoader("/resources/models/ctverecek.obj");
        try {
            ceiling_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.ceilingGeometry = Geometry.create(gl, ceiling_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader table_model = new ObjLoader("/resources/models/table.obj");
        try {
            table_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.tableGeometry = Geometry.create(gl, table_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader clock_model = new ObjLoader("/resources/models/clock.obj");
        try {
            clock_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.clockGeometry = Geometry.create(gl, clock_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader chair_model = new ObjLoader("/resources/models/chair.obj");
        try {
            chair_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.chairGeometry = Geometry.create(gl, chair_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader door_model = new ObjLoader("/resources/models/door.obj");
        try {
            door_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.doorGeometry = Geometry.create(gl, door_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader imageframe_model = new ObjLoader("/resources/models/imageframe.obj");
        try {
            imageframe_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.imageframeGeometry = Geometry.create(gl, imageframe_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader imageframe2_model = new ObjLoader("/resources/models/imageframe.obj");
        try {
            imageframe2_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        positionAttribLoc = gl.glGetAttribLocation(teapotProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(teapotProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(teapotProgram, "tex_coord");
        this.imageframe2Geometry = Geometry.create(gl, imageframe2_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader flashlight_model = new ObjLoader("/resources/models/flashlight.obj");
        try {
            flashlight_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        positionAttribLoc = gl.glGetAttribLocation(teapotProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(teapotProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(teapotProgram, "tex_coord");
        this.flashlightGeometry = Geometry.create(gl, flashlight_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader window_model = new ObjLoader("/resources/models/window.obj");
        try {
            window_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.windowGeometry = Geometry.create(gl, window_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader roof_model = new ObjLoader("/resources/models/roof.obj");
        try {
            roof_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(teapotProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(teapotProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(teapotProgram, "tex_coord");
        this.roofGeometry = Geometry.create(gl, roof_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader doorframe_model = new ObjLoader("/resources/models/doorframe.obj");
        try {
            doorframe_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.doorframeGeometry = Geometry.create(gl, doorframe_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader sphere_model = new ObjLoader("/resources/models/sphere.obj");
        try {
            sphere_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.sphereGeometry = Geometry.create(gl, sphere_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader cube_model = new ObjLoader("/resources/models/cube.obj");
        try {
            cube_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.cubeGeometry = Geometry.create(gl, cube_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader cylinder_model = new ObjLoader("/resources/models/cylinder.obj");
        try {
            cylinder_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.cylinderGeometry = Geometry.create(gl, cylinder_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader fish_model = new ObjLoader("/resources/models/fish.obj");
        try {
            fish_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(bedProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(bedProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(bedProgram, "tex_coord");
        this.fishGeometry = Geometry.create(gl, fish_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        ObjLoader aquarium_model = new ObjLoader("/resources/models/cube.obj");
        try {
            aquarium_model.load();
        } catch (IOException ex) {
            Logger.getLogger(Scene.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        positionAttribLoc = gl.glGetAttribLocation(aquariumProgram,"position");
        normalAttribLoc = gl.glGetAttribLocation(aquariumProgram, "normal");
        textureAttribLoc = gl.glGetAttribLocation(aquariumProgram, "tex_coord");
        this.aquariumGeometry = Geometry.create(gl, aquarium_model, positionAttribLoc, normalAttribLoc, textureAttribLoc);
        
        
        
        
        alpha1 = loadTexture(gl, this.getClass().getResource("/resources/textures/alpha_circle.png"), TextureIO.PNG);
        alpha2 = loadTexture(gl, this.getClass().getResource("/resources/textures/alpha2.png"), TextureIO.PNG);
        lenna = loadTexture(gl, this.getClass().getResource("/resources/textures/Lenna.png"), TextureIO.PNG);
        
        strop = loadTexture(gl, this.getClass().getResource("/resources/textures/strop.JPG"), TextureIO.JPG); 
        postel = loadTexture(gl, this.getClass().getResource("/resources/textures/BedTexture.JPG"), TextureIO.JPG); 
        stul = loadTexture(gl, this.getClass().getResource("/resources/textures/Wood_Table_C.jpg"), TextureIO.JPG); 
        zidle = loadTexture(gl, this.getClass().getResource("/resources/textures/TexturaZidle.jpg"), TextureIO.JPG); 
        dvere = loadTexture(gl, this.getClass().getResource("/resources/textures/doorTexture.jpg"), TextureIO.JPG); 
        akvarko = loadTexture(gl, this.getClass().getResource("/resources/textures/aquariumTexture.jpg"), TextureIO.JPG);
        rybka = loadTexture(gl, this.getClass().getResource("/resources/textures/GOLDFISH.jpg"), TextureIO.JPG);
        bila = loadTexture(gl, this.getClass().getResource("/resources/textures/white.png"), TextureIO.PNG);
        okno = loadTexture(gl, this.getClass().getResource("/resources/textures/window.png"), TextureIO.PNG);
        strecha = loadTexture(gl, this.getClass().getResource("/resources/textures/roofTexture.jpg"), TextureIO.JPG); 
        hodiny = loadTexture(gl, this.getClass().getResource("/resources/textures/clockTexture.png"), TextureIO.PNG);
        napis = loadTexture(gl, this.getClass().getResource("/resources/textures/mujNapis.png"), TextureIO.PNG);
        baterka  = loadTexture(gl, this.getClass().getResource("/resources/textures/FlashlightTexture.jpg"), TextureIO.JPG);
        
        alpha1.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        alpha2.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        lenna.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        
        strop.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        postel.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        stul.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        zidle.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        dvere.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        akvarko.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        rybka.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        bila.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        okno.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        strecha.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        hodiny.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        napis.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
                
        baterka.bind(gl);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        gl.glGenerateMipmap(GL_TEXTURE_2D);
        
        
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        framebuffer = new FBObject();
        framebuffer.init(gl, width, height, 0);
        framebuffer.bind(gl);
        framebuffer.attachRenderbuffer(gl, GL_DEPTH_COMPONENT24);
        sceneTexAttachment = framebuffer.attachTexture2D(gl, 0, true);
        framebuffer.unbind(gl);
        
        framebuffer2 = new FBObject();
        framebuffer2.init(gl, width, height, 0);
        framebuffer2.bind(gl);
        framebuffer2.attachRenderbuffer(gl, GL_DEPTH_COMPONENT24);
        sceneTexAttachment2 = framebuffer2.attachTexture2D(gl, 0, true);
        framebuffer2.unbind(gl);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // not used
    }

    private float t = 0;

    @Override
    public void display(GLAutoDrawable drawable) {
        
        GL3 gl = drawable.getGL().getGL3();
        
        // animate variables
        if (animator.isAnimating()) {
            t += 0.02f;
        }
        
        // set perspective projection
        Mat4 projection = Matrices.perspective(60.0f, (float) width / (float) height, 1.0f, 500.0f);

        // set view transform based on camera position and orientation
        Vec3 yAxis = new Vec3(0.0f, 1.0f, 0.0f);
        Mat4 view = Matrices.lookAt(camera.getEyePosition(), Vec3.VEC3_ZERO, yAxis);

        // get projection * view (VP) matrix
        Mat4 vp = Mat4.MAT4_IDENTITY;
        vp = vp.multiply(projection);
        vp = vp.multiply(view);

        
        Vec4 light1Pos = new Vec4(3.0f, 2.0f, 0.0f, 0.0f);
        Vec4 light2Pos = new Vec4(-3.0f, 2.0f, 0.0f, 0.0f);
        Vec4 light3Pos = new Vec4(-12.0f, 0.0f, 5.0f, 0.1f);

        gl.glUseProgram(teapotProgram);

        
        gl.glUniform4f(light1PositionLoc, light1Pos.getX(), light1Pos.getY(),
                light1Pos.getZ(), light1Pos.getW());
        gl.glUniform4f(light2PositionLoc, light2Pos.getX(), light2Pos.getY(),
                light2Pos.getZ(), light2Pos.getW());
        
        gl.glUniform4f(light3PositionLoc, light3Pos.getX(), light3Pos.getY(),
                light3Pos.getZ(), light3Pos.getW());

        //nastveni barvy svetla
        gl.glUniform3f(light1AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light1DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light1SpecularColorLoc, 1.0f, 1.0f, 1.0f);
        
        gl.glUniform3f(light2AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light2DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light2SpecularColorLoc, 1.0f, 1.0f, 1.0f);
        
        gl.glUniform3f(light3AmbientColorLoc, 0.6f, 0.6f, 0.6f);
        gl.glUniform3f(light3DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light3SpecularColorLoc, 1.0f, 1.0f, 1.0f);
        
        gl.glUniform3f(light3DirectionLoc, 1.0f, 0.0f, 0.0f);
        gl.glUniform1f(light3CutOffLoc, 0.8f);

        gl.glUniform3fv(eyePositionLoc, 1, camera.getEyePosition().getBuffer());

        gl.glUseProgram(0);
        
        
        
        gl.glUseProgram(wallProgram);
        
        gl.glUniform4f(light1PositionLoc, light1Pos.getX(), light1Pos.getY(),
                light1Pos.getZ(), light1Pos.getW());
        gl.glUniform3f(light1AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light1DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light1SpecularColorLoc, 1.0f, 1.0f, 1.0f);
        
        gl.glUniform4f(light2PositionLoc, light2Pos.getX(), light2Pos.getY(),
                light2Pos.getZ(), light2Pos.getW());
        gl.glUniform3f(light2AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light2DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light2SpecularColorLoc, 1.0f, 1.0f, 1.0f);
        
        gl.glUniform3fv(eyePositionLoc, 1, camera.getEyePosition().getBuffer());
        gl.glUseProgram(0);
        
        gl.glUseProgram(bedProgram);
        gl.glUniform4f(light1PositionLoc, light1Pos.getX(), light1Pos.getY(),
                light1Pos.getZ(), light1Pos.getW());
        gl.glUniform3f(light1AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light1DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light1SpecularColorLoc, 1.0f, 1.0f, 1.0f);
        
        gl.glUniform4f(light2PositionLoc, light2Pos.getX(), light2Pos.getY(),
                light2Pos.getZ(), light2Pos.getW());
        gl.glUniform3f(light2AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light2DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light2SpecularColorLoc, 1.0f, 1.0f, 1.0f);

        gl.glUniform3fv(eyePositionLoc, 1, camera.getEyePosition().getBuffer());
        gl.glUseProgram(0);
        
        gl.glUseProgram(aquariumProgram);
        gl.glUniform4f(light1PositionLoc, light1Pos.getX(), light1Pos.getY(),
                light1Pos.getZ(), light1Pos.getW());
        gl.glUniform3f(light1AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light1DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light1SpecularColorLoc, 1.0f, 1.0f, 1.0f);
        
        gl.glUniform4f(light2PositionLoc, light2Pos.getX(), light2Pos.getY(),
                light2Pos.getZ(), light2Pos.getW());
        gl.glUniform3f(light2AmbientColorLoc, 0.3f, 0.3f, 0.3f);
        gl.glUniform3f(light2DiffuseColorLoc, 1.0f, 1.0f, 1.0f);
        gl.glUniform3f(light2SpecularColorLoc, 1.0f, 1.0f, 1.0f);

        gl.glUniform3fv(eyePositionLoc, 1, camera.getEyePosition().getBuffer());
        gl.glUseProgram(0);
        
        Vec3 mirrorPosition = new Vec3(-5, 0, 9.98f);
        Vec3 roomTranslation = new Vec3(0, -5, 0); 
        
        // draw filled polygons or lines
        gl.glPolygonMode(GL_FRONT_AND_BACK, mode);
        
        

        
        for(int i = 0; i < 2; i++){
        if(i == 0) {
            framebuffer.bind(gl);
            view = Matrices.lookAt(//inverze(*(-1)) v x-ove souradnici vektoru eye a target jsou kvuli otaceni zrcadloveho obrazu podle x pomoci scale(-1,1,1)
                new Vec3(-mirrorPosition.getX(), mirrorPosition.getY(), mirrorPosition.getZ()),
                new Vec3(-2*mirrorPosition.getX()+camera.getEyePosition().getX(), 2*mirrorPosition.getY()-camera.getEyePosition().getY(), camera.getEyePosition().getZ()),
                yAxis);
            view = view.multiply(scale(-1,1,1));
        } //nekresli na obrazovku, ale do navazaneho bufferu "framebuffer"}
        if(i == 1) {
            framebuffer2.bind(gl);
            view = Matrices.lookAt(camera.getEyePosition(), Vec3.VEC3_ZERO, yAxis);
        }
        
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        
        
        
       
        Material material = new Material(
                new Vec3(1.0f, 1f, 1f),
                new Vec3(1.0f, 1f, 1f),
                new Vec3(0.2f, 0.2f, 0.2f),
                100.0f);
        
        
        Mat4 model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 5, -9.95f));
        model = model.translate(roomTranslation);
        Mat4 mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(0, 1, 0)));
        //mvp = mvp.multiply(Matrices.rotate((float)(Math.PI), new Vec3(1, 0, 0)));
        mvp = mvp.multiply(scale(0.05f,0.05f,0.05f));
        drawWindow(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 10.005f, 0));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(5,5,5));
        drawRoof(gl, model, mvp, material);
        
        
        /*Mat4 */model = Mat4.MAT4_IDENTITY.translate(new Vec3(10, 0, 10));
        model = model.translate(roomTranslation);
        /*Mat4 */mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(0, 0, 1)));
        mvp = mvp.multiply(scale(10,1,20));
        drawWall(gl, model, mvp, material);
        
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(-10, 0, -10));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(0, 0, 1)));
        mvp = mvp.multiply(Matrices.rotate((float)(Math.PI), new Vec3(1, 0, 0)));
        mvp = mvp.multiply(scale(10,1,20));
        drawWall(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(10, 0, -10));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(0, 0, 1)));
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(1, 0, 0)));
        mvp = mvp.multiply(scale(10,1,20));
        drawWall(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(-10, 0, 10));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(0, 0, 1)));
        mvp = mvp.multiply(Matrices.rotate((float)(1.5f*Math.PI), new Vec3(1, 0, 0)));
        mvp = mvp.multiply(scale(10,1,20));
        drawWall(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(10, 10, 10));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(Math.PI), new Vec3(0, 0, 1)));
        mvp = mvp.multiply(scale(20,1,20));
        drawCeiling(gl, model, mvp, material);
        
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(-10, 0, 10));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(20,1,20));
        drawCeiling(gl, model, mvp, material);
        
        
        
        material = new Material(
                new Vec3(1.0f, 1f, 1f),
                new Vec3(0.2f, 0.2f, 0.2f),
                new Vec3(0.2f, 0.2f, 0.2f),
                100.0f);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 7.15f, 9.9f));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(1, 0, 0)));
        mvp = mvp.multiply(Matrices.rotate((float)(Math.PI), new Vec3(0, 0, 1)));
        mvp = mvp.multiply(scale(0.12f,0.12f,0.12f));
        drawClock(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 2, 9.7f)).multiply(Matrices.rotate((float)(t), new Vec3(0, 0, 1)));
        model = model.translate(new Vec3((float)(0.15* Math.sin(t)) ,(float)(0.15* Math.cos(t) + 7 - 1.7),0));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(0.05f,0.3f,0.05f));
        drawCylinder(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 2, 9.7f)).multiply(Matrices.rotate((float)(2), new Vec3(0, 0, 1)));
        model = model.translate(new Vec3((float)(0.15* Math.sin(2)) ,(float)(0.15* Math.cos(2) + 7 - 1.8),0));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(0.05f,0.2f,0.05f));
        drawCylinder(gl, model, mvp, material);
        
        material = new Material(
                new Vec3(1.0f, 1f, 1f),
                new Vec3(1.0f, 1f, 1f),
                new Vec3(1.0f, 1.0f, 1.0f),
                100.0f);
        
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(-7.5f, 1.38f, -5.5f));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(2,2,2));
        drawBed(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(7.5f, 1.38f, -5.5f));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(2,2,2));
        drawBed(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 0, 0));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(6,6,6));
        drawTable(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(5, 0, 0));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(3,3,3));
        drawChair(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 0, -5));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        //mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(3,3,3));
        drawChair(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(5, 0, 10));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        //mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(2,2,2));
        drawDoor(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 3.8f, (float) (0.4*Math.sin(t))) );
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(1.5*Math.PI), new Vec3(0, 1, 0)));
        if(Math.cos(t) > 0 && Math.cos(t) < 0.5) mvp = mvp.multiply(Matrices.rotate((float)((0.5 - Math.cos(t))*2*Math.PI), new Vec3(0, 1, 0)));//nastava, kdyz se ryba pohybuje dopredu, ale jen pomalu, tj na zacatku a konci pohybu dopredu, maximalni otoceni, tj. pi, nastava pri cos t = 0, tj na absolutnim zacatku a konci drahy
        if(Math.cos(t) < 0) mvp = mvp.multiply(Matrices.rotate((float)(Math.PI), new Vec3(0, 1, 0)));
        //mvp = mvp.multiply(scale(10,10,10));
        mvp = mvp.multiply(scale(0.002f,0.002f,0.002f));
        drawFish(gl, model, mvp, material);
        
        Material material1 = new Material(
                new Vec3(1.5f, 1.5f, 0.0f),
                new Vec3(0.01f, 0.01f, 0.0f),
                new Vec3(0.01f, 0.01f, 0.0f),
                100.0f);
        
        Material material2 = new Material(
                new Vec3(1.0f, 1f, 1f),
                new Vec3(1.0f, 1f, 1f),
                new Vec3(1.0f, 1.0f, 1.0f),
                100.0f);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(light1Pos.getX(), light1Pos.getY(),light1Pos.getZ()));
        //model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        //mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(1,1,1));
        drawLuster(gl, model, mvp, material1, material2);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(light2Pos.getX(), light2Pos.getY(),light2Pos.getZ()));
        //model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        //mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(1,1,1));
        drawLuster(gl, model, mvp, material1, material2);
        
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(-10.1f, 0, 5));
        //model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(2,2,2));
        drawPicture(gl, model, mvp, material);
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(light3Pos.getX()-2, light3Pos.getY(),light3Pos.getZ()));
        //model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(Matrices.rotate((float)(Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(0.2f,0.2f,0.2f));
        drawFlashlight(gl, model, mvp, material);
        
        if(i == 1){
            model = Mat4.MAT4_IDENTITY.translate(mirrorPosition);
            mvp = projection.multiply(view).multiply(model);
            //mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(0, 1, 0)));
            mvp = mvp.multiply(scale(1.5f,1.5f,1));
            drawMirror(gl, model, mvp, material);
        }
        
        
        
        
        
        //akvarko se musi kreslit az nakonec, aby skrz nej byly videt vsechny ostatni objekty 
        gl.glEnable(GL_BLEND);
        gl.glEnable(GL_CULL_FACE); //nutne, aby byl skrz akvarko videt jeho ram na druhe strane
        
        model = Mat4.MAT4_IDENTITY.translate(new Vec3(0, 3.8f, 0));
        model = model.translate(roomTranslation);
        mvp = projection.multiply(view).multiply(model);
        mvp = mvp.multiply(scale(1,1,1));
        
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glCullFace(GL_FRONT);
        drawAquarium(gl, model, mvp, material);
        gl.glCullFace(GL_BACK);
        drawAquarium(gl, model, mvp, material);
        
        gl.glDisable(GL_CULL_FACE);
        gl.glDisable(GL_BLEND);
        
        
        if(i == 0) {
            framebuffer.unbind(gl);
        } //kresli zas na obrazovku}
        if(i == 1) {
            framebuffer2.unbind(gl);
        } //kresli zas na obrazovku}
        }
        
        
        gl.glDisable(GL_DEPTH_TEST);
        gl.glUseProgram(quadProgram);
        gl.glClear(GL_COLOR_BUFFER_BIT);
        gl.glBindVertexArray(quadArray);
        
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, sceneTexAttachment2.getName());
        //lenna.bind(gl);
        gl.glUniform1i(sceneTexLoc, 0);
        
        gl.glActiveTexture(GL_TEXTURE1);
        napis.bind(gl);
        gl.glUniform1i(sceneTexLoc2, 1);
        
        gl.glUniform1f(tLoc, t);
        
        gl.glDrawArrays(GL_TRIANGLES, 0, 6);
        gl.glUseProgram(0);
        gl.glEnable(GL_DEPTH_TEST);
        
        gl.glBindVertexArray(joglArray);
        
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        
    }

    private Mat3 getMat3(Mat4 m) {
        Vec4 col0 = m.getColumn(0);
        Vec4 col1 = m.getColumn(1);
        Vec4 col2 = m.getColumn(2);
        return new Mat3(
                col0.getX(), col0.getY(), col0.getZ(),
                col1.getX(), col1.getY(), col1.getZ(),
                col2.getX(), col2.getY(), col2.getZ());
    }

    private void drawTeapot(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(teapotProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(teapot_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        lenna.bind(gl);

        gl.glUniform1i(teapot_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        teapotGeometry.draw(gl);
        gl.glUseProgram(0);
    }

    private void drawFlashlight(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(teapotProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(teapot_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        baterka.bind(gl);

        gl.glUniform1i(teapot_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        flashlightGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawCeiling(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        strop.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        ceilingGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawWall(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(wallProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(wall_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        strop.bind(gl);

        gl.glUniform1i(wall_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        wallGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawBed(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        postel.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        bedGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    private void drawMirror(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        mvp = mvp.multiply(scale(1,1,0.01f));
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        gl.glBindTexture(GL_TEXTURE_2D, sceneTexAttachment.getName());//postel.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        cubeGeometry.draw(gl);
        mvp = mvp.multiply(scale(2.1f,2.1f / 1.65f,100));
        mvp = mvp.translate(new Vec3(0.53f,-0.01f,0));
        mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(1, 0, 0)));
        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());
        dvere.bind(gl);
        
        
        imageframeGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawPicture(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
       // mvp = mvp.multiply(scale(1,1.65f,0.01f));
        mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(0.01f,1,1));
        gl.glUseProgram(teapotProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(teapot_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        lenna.bind(gl);

        gl.glUniform1i(teapot_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());
        cubeGeometry.draw(gl);
        mvp = mvp.multiply(Matrices.rotate((float)(0.5f*Math.PI), new Vec3(0, 1, 0)));
        mvp = mvp.multiply(scale(2.1f,1.27f,50*1));
       // mvp = mvp.multiply(scale(2.1f,2.1f / 1.65f,100));
        mvp = mvp.translate(new Vec3(0.53f,-0.01f,0));
        mvp = mvp.multiply(Matrices.rotate((float)(-0.5f*Math.PI), new Vec3(1, 0, 0)));
        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());
        dvere.bind(gl);
        
        
        imageframe2Geometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawTable(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        stul.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        tableGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawClock(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        hodiny.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        clockGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawChair(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        zidle.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        chairGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    private void drawDoor(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        dvere.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        doorGeometry.draw(gl);
        
        strop.bind(gl);
        doorframeGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawWindow(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        okno.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        windowGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawRoof(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(teapotProgram);
        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(teapot_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        strecha.bind(gl);

        gl.glUniform1i(teapot_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());
        
        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        roofGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawLuster(GL3 gl, Mat4 model, Mat4 mvp, Material material1, Material material2) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        bila.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material1.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material1.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material1.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material1.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        sphereGeometry.draw(gl);
        
        mvp = mvp.multiply(scale(0.05f,1,0.05f));
        mvp = mvp.translate(new Vec3(0, 1.95f, 0));
        
        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());
        gl.glUniform3fv(materialAmbientColorLoc, 1, material2.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material2.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material2.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material2.getShininess());
        
        cylinderGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawFish(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        rybka.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        fishGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawCylinder(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(bedProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha1.bind(gl);
        
        gl.glUniform1i(bed_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        bila.bind(gl);

        gl.glUniform1i(bed_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        cylinderGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    private void drawAquarium(GL3 gl, Mat4 model, Mat4 mvp, Material material) {
        gl.glUseProgram(aquariumProgram);

        gl.glActiveTexture(GL_TEXTURE0);
        alpha2.bind(gl);
        
        gl.glUniform1i(aquarium_alpha_tex_loc, 0);

        gl.glActiveTexture(GL_TEXTURE1);
        akvarko.bind(gl);

        gl.glUniform1i(aquarium_color_tex_loc, 1);

        Mat3 n = MatricesUtils.inverse(getMat3(model).transpose());
        gl.glUniformMatrix3fv(nLoc, 1, false, n.getBuffer());

        gl.glUniformMatrix4fv(modelLoc, 1, false, model.getBuffer());

        gl.glUniform3fv(materialAmbientColorLoc, 1, material.getAmbientColor().getBuffer());
        gl.glUniform3fv(materialDiffuseColorLoc, 1, material.getDiffuseColor().getBuffer());
        gl.glUniform3fv(materialSpecularColorLoc, 1, material.getSpecularColor().getBuffer());
        gl.glUniform1f(materialShininessLoc, material.getShininess());

        gl.glUniformMatrix4fv(mvpLoc, 1, false, mvp.getBuffer());

        aquariumGeometry.draw(gl);
        gl.glUseProgram(0);
    }
    
    
    private Texture loadTexture(GL3 gl, URL url, String suffix) {
        Texture texture;
        try {
            TextureData data = TextureIO.newTextureData(gl.getGLProfile(), url, false, suffix);
            texture = new Texture(gl, data);
            return texture;
        } catch (IOException ex) {
            System.err.println("File not found");
        }
        return null;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();

        this.width = width;
        this.height = height;
        
        framebuffer.reset(gl, width, height, 0);
        framebuffer2.reset(gl, width, height, 0);
        
        gl.glViewport(0, 0, width, height); 
    }

    private int loadShader(GL3 gl, String filename, int shaderType) throws IOException {
        String source = readAllFromResource(filename);
        int shader = gl.glCreateShader(shaderType);

        // create and compile GLSL shader
        gl.glShaderSource(shader, 1, new String[]{source}, new int[]{source.length()}, 0);
        gl.glCompileShader(shader);

        // check GLSL shader compile status
        int[] status = new int[1];
        gl.glGetShaderiv(shader, GL_COMPILE_STATUS, status, 0);
        if (status[0] == GL_FALSE) {
            int[] length = new int[1];
            gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, length, 0);

            byte[] log = new byte[length[0]];
            gl.glGetShaderInfoLog(shader, length[0], length, 0, log, 0);

            String error = new String(log, 0, length[0]);
            System.err.println(error);
        }

        return shader;
    }

    private int loadProgram(GL3 gl, String vertexShaderFile, String fragmentShaderFile) throws IOException {
        // load vertex and fragment shaders (GLSL)
        int vs = loadShader(gl, vertexShaderFile, GL_VERTEX_SHADER);
        int fs = loadShader(gl, fragmentShaderFile, GL_FRAGMENT_SHADER);

        // create GLSL program, attach shaders and compile it
        int program = gl.glCreateProgram();
        gl.glAttachShader(program, vs);
        gl.glAttachShader(program, fs);
        gl.glLinkProgram(program);

        int[] linkStatus = new int[1];
        gl.glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == GL_FALSE) {
            int[] length = new int[1];
            gl.glGetProgramiv(program, GL_INFO_LOG_LENGTH, length, 0);

            byte[] log = new byte[length[0]];
            gl.glGetProgramInfoLog(program, length[0], length, 0, log, 0);

            String error = new String(log, 0, length[0]);
            System.err.println(error);
        }

        return program;
    }

    private String readAllFromResource(String resource) throws IOException {
        InputStream is = Scene.class.getResourceAsStream(resource);
        if (is == null) {
            throw new IOException("Resource not found: " + resource);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        int c;
        while ((c = reader.read()) != -1) {
            sb.append((char) c);
        }

        return sb.toString();
    }
    
    
    
    private Mat4 translate(float x, float y, float z) {
        Mat4 m = new Mat4(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        );
        m = m.transpose();
        return m;
    }
    private Mat4 scale(float x, float y, float z) {
        Mat4 m = new Mat4(
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1
        );
        m = m.transpose();
        return m;
    }
    
}
