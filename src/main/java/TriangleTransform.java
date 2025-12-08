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
    int courtTexID = 0;
    int powerUpGrowTex;
    int powerUpFlashTex;

    // new variables for VBO's
    private int[] vertBufIdBall = new int[1];
    private int[] vertBufIdPlayer = new int[1];
    private int[] vertBufIdPlayingField = new int[1];
    private int[] vertBufId_0 = new int[1];
    private int[] vertBufId_1 = new int[1];
    private int[] vertBufId_2 = new int[1];
    private int[] vertBufId_3 = new int[1];
    private int[] vertBufIdPowerUp = new int[1];
    private int vertNoBall = 0;
    private int vertNoPlayer = 0;
    private int vertNoPlayingField = 0;
    private int vertNo_0 = 0;
    private int vertNo_1 = 0;
    private int vertNo_2 = 0;
    private int vertNo_3 = 0;
    private int vertNoPowerUp = 0;


    public void drawScoreZero (GL2 gl, float x, float y, float rotation) {
        gl.glPushMatrix();

        gl.glTranslatef(x, y - 0.01f, -2.0f);
        gl.glRotatef(rotation,0.0f,0.0f, 1.0f);
        gl.glScalef(0.25f,0.25f,0.25f);

        // activating ScoreZero VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_0[0]);
        int stride = (3+4+2+3)*Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3*Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNo_0);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glPopMatrix();
    }

    public void drawScoreOne (GL2 gl, float x, float y, float rotation) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y - 0.01f, -2.0f);
        gl.glRotatef(rotation,0.0f,0.0f, 1.0f);
        gl.glScalef(0.25f,0.25f,0.25f);

        // activating ScoreZero VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_1[0]);
        int stride = (3+4+2+3)*Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3*Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNo_1);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glPopMatrix();
    }

    public void drawScoreTwo (GL2 gl, float x, float y, float rotation) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y - 0.01f, -2.0f);
        gl.glRotatef(rotation,0.0f,0.0f, 1.0f);
        gl.glScalef(0.25f,0.25f,0.25f);

        // activating ScoreZero VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_2[0]);
        int stride = (3+4+2+3)*Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3*Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNo_2);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glPopMatrix();
    }

    public void drawScoreThree (GL2 gl, float x, float y, float rotation) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y - 0.01f, -2.0f);
        gl.glRotatef(rotation,0.0f,0.0f, 1.0f);
        gl.glScalef(0.25f,0.25f,0.25f);

        // activating ScoreZero VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_3[0]);
        int stride = (3+4+2+3)*Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3*Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNo_3);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glPopMatrix();
    }


    public void drawCube(GL2 gl, float x, float y, float rotation, float scale) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, -2.0f);
        gl.glRotatef(rotation, 0, 0, 1);
        gl.glScalef(scale, scale, scale);

        // activating Ball VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdBall[0]);
        int stride = (3+4+2+3)*Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3*Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNoBall);
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNoPlayer);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);

        gl.glPopMatrix();
    }

    public void drawBat(GL2 gl, Player player, float x, float y, float rotation, float scale) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, -2.0f);
        gl.glRotatef(rotation, 0, 0, 1);
        gl.glScalef(scale / 2, player.paddleHeight, scale / 2);

        // activating Player VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdPlayer[0]);
        int stride = (3+4+2+3)*Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3*Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNoPlayer);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);

        gl.glPopMatrix();
    }

    public void drawPlayingField(GL2 gl, float x, float y, float rotation, float scale) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, courtTexID);

        gl.glPushMatrix();
        gl.glTranslatef(x, y, -1.2f);
        gl.glRotatef(rotation, 0, 1, 0);
        gl.glScalef(scale, scale, scale);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glLineWidth(1.0f);

        // activating PlayingField VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdPlayingField[0]);
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
        offsetVBO = (3 + 4) * Buffers.SIZEOF_FLOAT;
        gl.glTexCoordPointer(2, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        // normals
        offsetVBO = 0 + (3+4+2)*Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNoPlayingField);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glPopMatrix();
    }

    public void drawPowerUp(GL2 gl, PowerUp powerUp, float scale) {
        if (!powerUp.active) return;

        int tex = (powerUp.type == 1) ? powerUpFlashTex : powerUpGrowTex;

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);

        gl.glPushMatrix();
        gl.glTranslatef(powerUp.posX, powerUp.posY, -2.0f);
        gl.glScalef(scale, scale, scale);

        // activating PowerUp VBO
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdPowerUp[0]);
        int stride = (3 + 4 + 2 + 3) * Buffers.SIZEOF_FLOAT;
        int offsetVBO = 0;

        // position
        gl.glVertexPointer(3, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        // color
        offsetVBO = 0 + 3 * Buffers.SIZEOF_FLOAT;
        gl.glColorPointer(4, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

        // texture
        offsetVBO = (3 + 4) * Buffers.SIZEOF_FLOAT;
        gl.glTexCoordPointer(2, GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        // normals
        offsetVBO = 0 + (3 + 4 + 2) * Buffers.SIZEOF_FLOAT;
        gl.glNormalPointer(GL2.GL_FLOAT, stride, offsetVBO);
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

        // render data
        gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vertNoPowerUp);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisable(GL2.GL_TEXTURE_2D);

        gl.glPopMatrix();
    }



    @Override
    public void init(GLAutoDrawable d) {
        game = new Game();
        GL2 gl = d.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST);

        courtTexID = loadTexture(d, "interstellar.png");
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
        float playingFieldVertexData[] = loadVertexData("court.vbo", perVertexFloats);
        float scoreZeroVertexData[] = loadVertexData("0.vbo", perVertexFloats);
        float scoreOneVertexData[] = loadVertexData("1.vbo", perVertexFloats);
        float scoreTwoVertexData[] = loadVertexData("2.vbo", perVertexFloats);
        float scoreThreeVertexData[] = loadVertexData("3.vbo", perVertexFloats);
        float powerUpVertexData[] = loadVertexData("box_tri.vbo", perVertexFloats);

        // generating Ball vertex VBO
        vertNoBall = ballVertexData.length / perVertexFloats;
        FloatBuffer dataIn1 = Buffers.newDirectFloatBuffer(ballVertexData.length);
        dataIn1.put(ballVertexData);
        dataIn1.flip();

        gl.glGenBuffers(1, vertBufIdBall, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdBall[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn1.capacity() * Buffers.SIZEOF_FLOAT, dataIn1, GL2.GL_STATIC_DRAW);

        // generating Player vertex VBO
        vertNoPlayer = playerVertexData.length / perVertexFloats;
        FloatBuffer dataIn2 = Buffers.newDirectFloatBuffer(playerVertexData.length);
        dataIn2.put(playerVertexData);
        dataIn2.flip();

        gl.glGenBuffers(1, vertBufIdPlayer, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdPlayer[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn2.capacity() *Buffers.SIZEOF_FLOAT, dataIn2, GL2.GL_STATIC_DRAW);

        // generating PlayingField vertex VBO
        vertNoPlayingField = playingFieldVertexData.length / perVertexFloats;
        FloatBuffer dataIn3 = Buffers.newDirectFloatBuffer(playingFieldVertexData.length);
        dataIn3.put(playingFieldVertexData);
        dataIn3.flip();

        gl.glGenBuffers(1, vertBufIdPlayingField, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdPlayingField[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn3.capacity() *Buffers.SIZEOF_FLOAT, dataIn3, GL2.GL_STATIC_DRAW);

        // generating ScoreZero vertex VBO
        vertNo_0 = scoreZeroVertexData.length / perVertexFloats;
        FloatBuffer dataIn4 = Buffers.newDirectFloatBuffer(scoreZeroVertexData.length);
        dataIn4.put(scoreZeroVertexData);
        dataIn4.flip();

        gl.glGenBuffers(1, vertBufId_0, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_0[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn4.capacity() *Buffers.SIZEOF_FLOAT, dataIn4, GL2.GL_STATIC_DRAW);

        // generating ScoreOne vertex VBO
        vertNo_1 = scoreOneVertexData.length / perVertexFloats;
        FloatBuffer dataIn5 = Buffers.newDirectFloatBuffer(scoreOneVertexData.length);
        dataIn5.put(scoreOneVertexData);
        dataIn5.flip();

        gl.glGenBuffers(1, vertBufId_1, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_1[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn5.capacity() *Buffers.SIZEOF_FLOAT, dataIn5, GL2.GL_STATIC_DRAW);

        // generating ScoreTwo vertex VBO
        vertNo_2 = scoreTwoVertexData.length / perVertexFloats;
        FloatBuffer dataIn6 = Buffers.newDirectFloatBuffer(scoreTwoVertexData.length);
        dataIn6.put(scoreTwoVertexData);
        dataIn6.flip();

        gl.glGenBuffers(1, vertBufId_2, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_2[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn6.capacity() *Buffers.SIZEOF_FLOAT, dataIn6, GL2.GL_STATIC_DRAW);

        // generating ScoreThree vertex VBO
        vertNo_3 = scoreThreeVertexData.length / perVertexFloats;
        FloatBuffer dataIn7 = Buffers.newDirectFloatBuffer(scoreThreeVertexData.length);
        dataIn7.put(scoreThreeVertexData);
        dataIn7.flip();

        gl.glGenBuffers(1, vertBufId_3, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufId_3[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn7.capacity() *Buffers.SIZEOF_FLOAT, dataIn7, GL2.GL_STATIC_DRAW);

        // generating PowerUp vertex VBO
        vertNoPowerUp = powerUpVertexData.length / perVertexFloats;
        FloatBuffer dataIn8 = Buffers.newDirectFloatBuffer(powerUpVertexData.length);
        dataIn8.put(powerUpVertexData);
        dataIn8.flip();

        gl.glGenBuffers(1, vertBufIdPowerUp, 0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertBufIdPowerUp[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) dataIn8.capacity() *Buffers.SIZEOF_FLOAT, dataIn8, GL2.GL_STATIC_DRAW);

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

        drawCube(gl, game.ball.posx, game.ball.posy, game.ball.rotation, 0.15f);
        drawBat(gl, game.player1, game.player1.posX, game.player1.posY, 270.0f, 1f);
        drawBat(gl, game.player2, game.player2.posX, game.player2.posY, 90.0f, 1f);

        drawPlayingField(gl, 0.0f, 0.0f, t, 2.0f);
        float offset = 0.01f;
        t += offset;

        drawPowerUp(gl, game.powerUp, 0.07f);

        switch (game.scoreP1) {
            case 0:
                drawScoreZero(gl, -0.1f, 1.0f, 0.0f);
                break;
            case 1:
                drawScoreOne(gl, -0.1f, 1.0f, 0.0f);
                break;
            case 2:
                drawScoreTwo(gl, -0.1f, 1.0f, 0.0f);
                break;
            case 3:
                drawScoreThree(gl, -0.1f, 1.0f, 0.0f);
                break;
        }

        switch (game.scoreP2) {
            case 0:
                drawScoreZero(gl, 0.1f, 1.0f, 0.0f);
                break;
            case 1:
                drawScoreOne(gl, 0.1f, 1.0f, 0.0f);
                break;
            case 2:
                drawScoreTwo(gl, 0.1f, 1.0f, 0.0f);
                break;
            case 3:
                drawScoreThree(gl, 0.1f, 1.0f, 0.0f);
                break;
        }
        game.step();

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
        float batRadius = player.paddleHeight * 0.19f;   // ~ one fifth of height
        float ballRadius = 0.04f;

        float batCenterX = player.posX;
        float batCenterY = player.posY;

        // distance ball to the bat center
        float dx = posx - batCenterX;
        float dy = posy - batCenterY;

        float dist2 = dx * dx + dy * dy;
        float minDist = batRadius + ballRadius;

        // rounded collision check
        if (dist2 > minDist * minDist) return;

        float dist = (float) Math.sqrt(dist2);

        // normal vector at hit point
        float nx = dx / dist;
        float ny = dy / dist;

        // vector reflection
        float dot = velx * nx + vely * ny;

        velx = velx - 2.0f * dot * nx;
        vely = vely - 2.0f * dot * ny;

        velx *= 1.04f;

        // push ball out of bat
        float overlap = minDist - dist;
        posx += overlap * nx;
        posy += overlap * ny;

        float hitOffsetY = posy - player.posY;
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
    float paddleHeight = 1f;
    float originalHeight = 1f;

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
