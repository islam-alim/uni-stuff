// This code example is created for educational purpose
// by Thorsten Thormaehlen (contact: www.thormae.de).
// It is distributed without any warranty.

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class Renderer2 implements GLEventListener {

    private GLU glu = new GLU();
    Game game;

    public float t = 0.0f;
    int texID = 0;
    int powerUpGrowTex;
    int powerUpFlashTex;

    // new variables for VBO's
    private int[] vertBufID = new int[1];
    private int texIdBall = 0;
    private int texIdPlayer = 0;
    private int vertNo = 0;

    float[] cube = {
            // front
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,

            // back
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            // top
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // bottom
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            // left
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // right
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
    };

    float[] colorCube = {
            //front
            0.75f,0.50f,
            1.00f,0.50f,
            1.00f,0.25f,
            0.75f,0.25f,

            // back
            0.50f,0.50f,
            0.25f,0.50f,
            0.25f,0.25f,
            0.50f,0.25f,

            //top
            0.50f,0.75f,
            0.25f,0.75f,
            0.25f,0.50f,
            0.50f,0.50f,

            //bottom
            0.25f,0.25f,
            0.25f,0.00f,
            0.50f,0.00f,
            0.50f,0.25f,

            //left
            0.00f,0.50f,
            0.00f,0.25f,
            0.25f,0.25f,
            0.25f,0.50f,

            //right
            0.75f,0.50f,
            0.75f,0.25f,
            0.50f,0.25f,
            0.50f,0.50f,
        };

    float[] score0Data = { 0.06f, 0.1f, 0.04f, 0.1f, 0.04f, -0.1f, 0.06f,
            -0.1f, -0.04f, 0.1f, -0.06f, 0.1f, -0.06f, -0.1f, -0.04f, -0.1f,
            0.05f, 0.1f, 0.05f, 0.08f, -0.05f, 0.08f, -0.05f, 0.1f, 0.05f,
            -0.08f, 0.05f, -0.1f, -0.05f, -0.1f, -0.05f, -0.08f };

    float[] score1Data = { 0.01f, 0.1f, -0.01f, 0.1f, -0.01f, -0.1f, 0.01f,
            -0.1f };

    float[] score2Data = { 0.06f, 0.1f, 0.04f, 0.1f, 0.04f, 0.0f,
            0.06f, 0.0f, -0.04f, 0.0f, -0.06f, 0.0f,
            -0.06f, -0.1f, -0.04f, -0.1f, 0.05f, 0.1f, 0.05f,
            0.08f, -0.05f, 0.08f, -0.05f, 0.1f, 0.05f, -0.08f, 0.05f,
            -0.1f, -0.05f, -0.1f, -0.05f, -0.08f, 0.05f,
            0.01f, 0.05f, -0.01f, -0.05f, -0.01f, -0.05f,
            0.01f };

    float[] score3Data = { 0.06f, 0.1f, 0.04f, 0.1f, 0.04f, -0.1f, 0.06f,
            -0.1f, 0.05f, 0.1f, 0.05f, 0.08f, -0.05f, 0.08f, -0.05f, 0.1f,
            0.05f, -0.08f, 0.05f, -0.1f, -0.05f, -0.1f, -0.05f, -0.08f, 0.05f,
            0.01f, 0.05f, -0.01f, -0.05f, -0.01f, -0.05f, 0.01f};


    public void draw2D (GL2 gl, float x, float y, float[] array, float rotation) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, -2.0f);
        gl.glRotatef(rotation,0.0f,0.0f, 1.0f);

        gl.glBegin(GL2.GL_QUADS);
        for (int i = 0; i < array.length; i += 2) {
            gl.glVertex3f(array[i], array[i + 1], 0.0f);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void drawCube(GL2 gl, float x, float y, float[] cube, float rotation, float scale) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, -2.0f);
        gl.glRotatef(rotation, 0, 0, 1);
        gl.glScalef(scale, scale, scale);

        gl.glBegin(GL2.GL_QUADS);

        float[][] colors = {
                {1.0f, 1.0f, 1.0f},
                {0.7f, 0.7f, 0.7f},
                {0.5f, 0.5f, 0.5f},
                {0.5f, 0.5f, 0.5f},
                {0.7f, 0.7f, 0.7f},
                {0.5f, 0.5f, 0.5f}
        };

        for (int face = 0; face < 6; face++) {
            gl.glColor3f(colors[face][0], colors[face][1], colors[face][2]);
            for (int i = 0; i < 4; i++) {
                int idx = face * 12 + i * 3;
                gl.glVertex3f(cube[idx], cube[idx + 1], cube[idx + 2]);
            }
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void drawBat(GL2 gl, Player player, float x, float y, float[] cube, float rotation, float scale) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, -2.0f);
        gl.glRotatef(rotation, 0, 0, 1);
        gl.glScalef(scale / 2, player.paddleHeight, scale / 2);

        gl.glBegin(GL2.GL_QUADS);

        float[][] colors = {
                {1.0f, 1.0f, 1.0f},
                {0.7f, 0.7f, 0.7f},
                {0.5f, 0.5f, 0.5f},
                {0.5f, 0.5f, 0.5f},
                {0.7f, 0.7f, 0.7f},
                {0.5f, 0.5f, 0.5f}
        };

        for (int face = 0; face < 6; face++) {
            gl.glColor3f(colors[face][0], colors[face][1], colors[face][2]);
            for (int i = 0; i < 4; i++) {
                int idx = face * 12 + i * 3;
                gl.glVertex3f(cube[idx], cube[idx + 1], cube[idx + 2]);
            }
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void drawPlayingField(GL2 gl, float x, float y, float[] cube, float[] colorCube, float rotation, float scale) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texID);

        gl.glPushMatrix();
        gl.glTranslatef(x, y, -1.2f);
        gl.glRotatef(rotation, 0, 1, 0);
        gl.glScalef(scale, scale, scale);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glLineWidth(1.0f);

        for (int face = 0; face < 6; face++) {
            int v = face * 12; // vertex index (4 vertices × 3 floats)
            int t = face * 8;  // texcoord index (4 coords × 2 floats)
            gl.glBegin(GL2.GL_POLYGON);
            // 4 vertices per face
            for (int vert = 0; vert < 4; vert++) {
                gl.glTexCoord2f(
                        colorCube[t + vert * 2],     // u
                        colorCube[t + vert * 2 + 1]  // v
                );
                gl.glVertex3f(
                        cube[v + vert * 3],          // x
                        cube[v + vert * 3 + 1],      // y
                        cube[v + vert * 3 + 2]       // z
                );
            }
            gl.glEnd();
        }


        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glPopMatrix();
    }

    float[] fullFaceUV = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
    };


    public void drawPowerUp(GL2 gl, PowerUp powerUp, float[] cube, float scale) {
        if (!powerUp.active) return;

        int tex = (powerUp.type == 1) ? powerUpFlashTex : powerUpGrowTex;

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);

        gl.glPushMatrix();
        gl.glTranslatef(powerUp.posX, powerUp.posY, -2.0f);
        gl.glScalef(scale, scale, scale);

        for (int face = 0; face < 6; face++) {
            int v = face * 12; // 4 vertices × 3 floats
            gl.glBegin(GL2.GL_QUADS);
            for (int vert = 0; vert < 4; vert++) {
                gl.glTexCoord2f(fullFaceUV[vert * 2], fullFaceUV[vert * 2 + 1]);
                gl.glVertex3f(cube[v + vert * 3], cube[v + vert * 3 + 1], cube[v + vert * 3 + 2]);
            }
            gl.glEnd();
        }

        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glPopMatrix();
    }



    @Override
    public void init(GLAutoDrawable d) {
        game = new Game();
        GL2 gl = d.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);

        texID = loadTexture(d, "interstellar.png");
        powerUpGrowTex = loadTexture(d, "powerup_icons_grow.png");
        powerUpFlashTex = loadTexture(d, "powerup_icons_flash.png");


        // assuming the following structure of input vertex data
        // struct Vertex {
        //   float position[3];
        //   float color[4];
        //   float texCoord[2];
        //   float normal[3];
        // };

        int perVertexFloats = (3+4+2+3);
        float ballVertexData[] = loadVertexData("ball.vbo", perVertexFloats);
        float playerVertexData[] = loadVertexData("player.vbo", perVertexFloats);

        vertNo = ballVertexData.length / perVertexFloats;
        FloatBuffer dataIn = Buffers.newDirectFloatBuffer(ballVertexData.length);
        dataIn.put(ballVertexData);
        dataIn.flip();

        // generating vertex VBO
        gl.glGenBuffers(1, vertBufID, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufID[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, dataIn.capacity()*Buffers.SIZEOF_FLOAT, dataIn, GL2.GL_STATIC_DRAW);

    }

    @Override
    public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
        GL2 gl = d.getGL().getGL2();

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float aspect = (float) width / (float) height;
        glu.gluPerspective(60, aspect, 1.0f, 5.0f);
    }


    @Override
    public void display (GLAutoDrawable d) {

        GL2 gl = d.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        drawCube(gl, game.ball.posx, game.ball.posy, cube, game.ball.rotation, 0.05f);
        drawBat(gl, game.player1, game.player1.posX, game.player1.posY, cube, 0.0f, 0.1f);
        drawBat(gl, game.player2, game.player2.posX, game.player2.posY, cube, 0.0f, 0.1f);

        drawPlayingField(gl, 0.0f, 0.0f, cube, colorCube, t, 2.0f);
        float offset = 0.01f;
        t += offset;

        drawPowerUp(gl, game.powerUp, cube, 0.07f);

        switch (game.scoreP1) {
            case 0:
                draw2D(gl, -0.1f, 1.0f, score0Data, 0.0f);
                break;
            case 1:
                draw2D(gl, -0.1f, 1.0f, score1Data, 0.0f);
                break;
            case 2:
                draw2D(gl, -0.1f, 1.0f, score2Data, 0.0f);
                break;
            case 3:
                draw2D(gl, -0.1f, 1.0f, score3Data, 0.0f);
                break;
        }

        switch (game.scoreP2) {
            case 0:
                draw2D(gl, 0.1f, 1.0f, score0Data, 0.0f);
                break;
            case 1:
                draw2D(gl, 0.1f, 1.0f, score1Data, 0.0f);
                break;
            case 2:
                draw2D(gl, 0.1f, 1.0f, score2Data, 0.0f);
                break;
            case 3:
                draw2D(gl, 0.1f, 1.0f, score3Data, 0.0f);
                break;
        }
        game.step();

        // activating VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufID[0]);
        int stride = (3+4+2+3)*Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3*Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // texture
        offsetVBO = 0 + (3+4)*Buffers.SIZEOF_FLOAT;
        gl.glTexCoordPointer(2, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);


        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNo);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisable(GL2.GL_TEXTURE_2D);

        gl.glFlush();
    }

    private float[] loadVertexData(String filename, int perVertexFloats) {

        float[] floatArray = new float[0];
        // read vertex data from file
        int vertSize = 0;
        try {
            InputStream is = new FileInputStream(new File(filename));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            if (line != null) {
                vertSize = Integer.parseInt(line);
                floatArray = new float[vertSize];
            }
            int i = 0;
            while ((line = br.readLine()) != null && i < floatArray.length) {
                floatArray[i] = Float.parseFloat(line);
                i++;
            }
            if (i != vertSize || (vertSize % perVertexFloats) != 0) {
                floatArray = new float[0];
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Can not find vbo data file " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return floatArray;
    }

    @Override
    public void dispose(GLAutoDrawable d) {
    }

    // returns a valid textureID on success, otherwise 0
    private int loadTexture(GLAutoDrawable d, String filename) {
        GL2 gl = d.getGL().getGL2(); // get the OpenGL 2 graphics context

        int width;
        int height;
        int level = 0;
        int border = 0;

        try{
            // open file
            FileInputStream fileInputStream = new FileInputStream(new File(filename));

            // read image
            BufferedImage bufferedImage = ImageIO.read(fileInputStream);
            fileInputStream.close();

            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

            // convert image to ByteBuffer
            int[] pixelIntData = new int[width * height];
            bufferedImage.getRGB(0, 0, width, height, pixelIntData, 0, width);
            ByteBuffer buffer = ByteBuffer.allocateDirect(pixelIntData.length * 4);
            buffer.order(ByteOrder.nativeOrder());
            // Unpack the data, each integer into 4 bytes of the ByteBuffer.
            // Also we need to vertically flip the image because the image origin
            // in OpenGL is the lower-left corner.
            for(int y=0; y < height; y++) {
                int k = (height-1-y) * width;
                for(int x=0; x < width; x++) {
                    buffer.put((byte)(pixelIntData[k]>>> 16));
                    buffer.put((byte)(pixelIntData[k]>>> 8));
                    buffer.put((byte)(pixelIntData[k]));
                    buffer.put((byte)(pixelIntData[k]>>> 24));
                    k++;
                }
            }
            buffer.rewind();

            // data is aligned in byte order
            gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

            //request textureID
            final int[] textureID = new int[1];
            gl.glGenTextures( 1, textureID, 0);

            // bind texture
            gl.glBindTexture(GL2.GL_TEXTURE_2D, textureID[0]);

            //define how to filter the texture (important but ignore for now)
            gl.glTexParameteri (GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameteri (GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);

            //texture colors should replace the original color values
            gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE); //GL_MODULATE

            // specify the 2D texture map
            gl.glTexImage2D(GL2.GL_TEXTURE_2D, level, GL2.GL_RGB, width, height, border, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);

            return textureID[0];
        } catch( FileNotFoundException e) {
            System.out.println("Can not find texture data file " + filename);
        } catch(IOException e) {
            e.printStackTrace( );
        }
        return 0;
    }
}


class MyGui2 extends JFrame {


    private Renderer2 renderer2;

    public void createGUI() {

        setTitle("Pong Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        setSize(1366,768);
        getContentPane().add(canvas);


        renderer2 = new Renderer2();
        canvas.addGLEventListener(renderer2);

        final FPSAnimator ani = new FPSAnimator(canvas, 60, true);


        canvas.setFocusable(true);
        canvas.requestFocusInWindow();

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int kc = e.getKeyCode();
                switch(kc) {
                    case KeyEvent.VK_W:
                        renderer2.game.player1.startMovingUp();
                        break;
                    case KeyEvent.VK_S:
                        renderer2.game.player1.startMovingDown();
                        break;
                    case KeyEvent.VK_P:
                        renderer2.game.player2.startMovingUp();
                        break;
                    case KeyEvent.VK_L:
                        renderer2.game.player2.startMovingDown();
                        break;
                    case KeyEvent.VK_SPACE:
                        renderer2.game = new Game();
                        renderer2.game.gameOver = false;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int kc = e.getKeyCode();
                switch (kc) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_S:
                        renderer2.game.player1.stopMoving();
                        break;
                    case KeyEvent.VK_P:
                    case KeyEvent.VK_L:
                        renderer2.game.player2.stopMoving();
                        break;
                }
            }
        });

        setVisible(true);
        ani.start();

    }

}

class Game {

    public Ball ball = new Ball(0, 0, 0.6f, 0.2f);
    public Player player1 = new Player(-0.9f, 0);
    public Player player2 = new Player(0.9f, 0);

    public PowerUp powerUp;
    public Player lastTouched = player1;
    public boolean powerUpActive = false;
    public long powerUpStartTime;
    public int powerUpType;

    int scoreP1 = 0;
    int scoreP2 = 0;
    boolean gameOver = true;

    public Game() {
        powerUp = new PowerUp("");
    }

    public void step() {

        if (!gameOver) {
            ball.move();
            player1.move();
            player2.move();

            ball.touches(player1);
            ball.touches(player2);

            if (scoreP1 >= 3 || scoreP2 >= 3) {
                gameOver = true;
            }

            // track last touched player
            if ((ball.oldPosX < player1.posX && ball.posx >= player1.posX) ||
                    (ball.oldPosX > player1.posX && ball.posx <= player1.posX))
                lastTouched = player1;

            if ((ball.oldPosX < player2.posX && ball.posx >= player2.posX) ||
                    (ball.oldPosX > player2.posX && ball.posx <= player2.posX))
                lastTouched = player2;



            // power-up collision
            if (powerUp.isHit(ball)) {
                powerUp.pickUp();
                applyPowerUp(lastTouched);
            }

            // power-up duration
            if (powerUpActive && System.currentTimeMillis() - powerUpStartTime > 4000) {
                removePowerUp(lastTouched);
            }

            // scoring
            if (ball.posx >= 1.0f) {
                scoreP1++;
                resetBall(-0.6f);
                spawnPowerUpIfNeeded();
            } else if (ball.posx <= -1.0f) {
                scoreP2++;
                resetBall(0.6f);
                spawnPowerUpIfNeeded();
            }
            powerUp.move();
        }
    }




    private void resetBall(float velX) {
        ball = new Ball(0, 0, velX, 0);

        powerUp.active = false;


        powerUp.posY = -1.0f;

        if (powerUpActive) {
            player1.removeSpeedBoost();
            player1.resetPaddleSize();
            player2.removeSpeedBoost();
            player2.resetPaddleSize();
        }
        powerUpActive = false;
    }


    private void spawnPowerUpIfNeeded() {
        if (!powerUp.active && !powerUpActive) {
            if ((scoreP1 == 1 && scoreP2 == 0) ||
                    (scoreP1 == 0 && scoreP2 == 1) ||
                    (scoreP1 == 2 && scoreP2 == 1) ||
                    (scoreP1 == 1 && scoreP2 == 2)) {

                powerUp.active = true;
                powerUp.posX = 0f;
                powerUp.posY = -1.0f;
                powerUp.type = (int)(Math.random() * 2) + 1;
                powerUp.velY = Math.abs(powerUp.velY);
            }
        }
    }

        private void applyPowerUp (Player player){
            powerUpActive = true;
            powerUpStartTime = System.currentTimeMillis();
            powerUpType = powerUp.type;

            switch (powerUpType) {
                case 1 -> player.applySpeedBoost();
                case 2 -> player.enlargePaddle();
            }
        }

        private void removePowerUp (Player player){
            powerUpActive = false;
            switch (powerUpType) {
                case 1 -> player.removeSpeedBoost();
                case 2 -> player.resetPaddleSize();
            }
        }

}

class Ball {

    public Ball(float posx, float posy, float velx, float vely) {
        this.posx = posx;
        this.posy = posy;
        this.velx = velx * 0.02f;
        this.vely = vely * 0.02f;
    }

    public float posx;
    public float posy;

    public float oldPosX;
    public float oldPosY;

    public float velx;
    public float vely;
    public float rotation = 0.0f;
    public float rotationspeed = 5.0f;

    public void move() {

        oldPosX = posx;
        oldPosY = posy;

        posx += velx;
        posy += vely;
        rotation += rotationspeed;

        if (posy >= 1.0f || posy <= -1.0f) {
            vely = -vely;
        }

        float maxSpeed = 0.1f;
        if (velx > maxSpeed) velx = maxSpeed;
        if (velx < -maxSpeed) velx = -maxSpeed;
    }

    public void touches(Player player) {
        boolean crossed = (oldPosX < player.posX && posx >= player.posX) ||
                (oldPosX > player.posX && posx <= player.posX);

        if (!crossed) return;

        float dy = Math.abs(this.posy - player.posY);
        float verticalThreshold = 0.25f;
        if (dy > verticalThreshold) return;

        float ballRadius = 0.04f;

        if (player.posX > 0) {
            posx = player.posX - ballRadius;
        } else {
            posx = player.posX + ballRadius;
        }

        velx = -velx * 1.04f;

        float hitOffsetY = this.posy - player.posY;
        float spinFactor = 15.0f;
        rotationspeed += hitOffsetY * spinFactor * Math.signum(-velx);

        float magnus = rotationspeed * 0.0015f;
        vely += magnus;

        float maxSpin = 200.0f;
        rotationspeed = Math.max(Math.min(rotationspeed, maxSpin), -maxSpin);
    }

}

class Player {
    float posX;
    float posY;
    float velY;
    float paddleHeight = 0.25f;
    float originalHeight = 0.25f;

    boolean speedBoost = false;
    boolean enlarge = false;

    public Player(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        this.velY = 0;
    }

    public void move() { posY += velY; }

    public void startMovingUp() { velY = speedBoost ? 0.08f : 0.04f; }
    public void startMovingDown() { velY = speedBoost ? -0.08f : -0.04f; }
    public void stopMoving() { velY = 0; }

    public void applySpeedBoost() { speedBoost = true; }
    public void removeSpeedBoost() { speedBoost = false; }

    public void enlargePaddle() { paddleHeight *= 1.5f; enlarge = true; }
    public void resetPaddleSize() { paddleHeight = originalHeight; enlarge = false; }
}


class PowerUp {

    public float posX = 0;
    public float posY = -1;
    public float velY = 0.025f;
    public boolean active = false;
    public int type; // 1 = speed, 2 = enlarge
    public String texID;

    public PowerUp(String texID) {
        this.texID = texID;
        type = (int)(Math.random() * 2) + 1;
    }

    public void move() {
        if (!active) return; // do nothing when not active

        posY += velY;
        if (posY > 1.0f || posY < -1.0f) velY = -velY;
    }


    public boolean isHit(Ball ball) {
        if (!active) return false;
        float dx = Math.abs(ball.posx - posX);
        float dy = Math.abs(ball.posy - posY);
        return dx < 0.05f && dy < 0.05f;
    }

    public void pickUp() {
        active = false;
    }
}

public class TriangleTransform {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.uiScale", "1.0");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MyGui2 myGUI2 = new MyGui2();
                myGUI2.createGUI();
            }
        });
    }
}
