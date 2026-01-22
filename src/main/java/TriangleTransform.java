// This code example is created for educational purpose
// by Thorsten Thormaehlen (contact: www.thormae.de).
// It is distributed without any warranty.

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class Renderer2 implements GLEventListener {

    Game game;

    public float t = 0.0f;
    public int modeVal = 0; // shading mode
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

    // VAO ID's
    private int[] vaoBall = new int[1];
    private int[] vaoPlayer = new int[1];
    private int[] vaoPlayingField = new int[1];
    private int[] vaoPowerUp = new int[1];
    private int powerUpVertexCount;
    private int[] vaoDigits = new int[4]; // 0,1,2,3
    private static final int STRIDE = (3+4+2+3) * Buffers.SIZEOF_FLOAT;

    // Shaders
    private int progID = 0;
    private int vertID = 0;
    private int fragID = 0;

    // Attribute Locations
    private int vertexLoc = 0;
    private int colorLoc = 0;
    private int texCoordLoc = 0;
    private int normalLoc = 0;

    // Uniform Locations
    private int projectionLoc = 0;
    private int modelviewLoc = 0;
    private int normalMatrixLoc = 0;
    private int texLoc = 0;
    private int shadeLoc = 0;
    private int lightDirLoc = 0;
    private int modeLoc = 0;
    private int metallicLoc = 0;
    private int roughnessLoc = 0;

    public void drawDigit(GL3 gl, float x, float y, int digit, int vertexCount) {
        uploadModel(gl, x, y, -2.0f, 0.0f, 0.25f, 0.25f, 0.25f);
        gl.glUniform1i(shadeLoc, 0);
        gl.glBindVertexArray(vaoDigits[digit]);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertexCount);
        gl.glBindVertexArray(0);
    }

    public void drawBall(GL3 gl, float x, float y, float rotationDeg, float scale) {
        uploadModel(gl, x, y, -2.0f, rotationDeg, scale, scale, scale);
        gl.glUniform1i(shadeLoc, 0);
        gl.glBindVertexArray(vaoBall[0]);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertNoBall);
        gl.glBindVertexArray(0);
    }

    public void drawBat(GL3 gl, Player player, float x, float y, float rotation, float scale) {

        uploadModel(gl, x, y, -2.0f, rotation, scale / 2.0f, player.paddleHeight, scale / 2.0f);
        gl.glUniform1i(shadeLoc, 0);
        gl.glBindVertexArray(vaoPlayer[0]);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertNoPlayer);
        gl.glBindVertexArray(0);
    }

    public void drawPlayingField(GL3 gl, float x, float y, float rotation, float scale) {

        gl.glBindTexture(GL.GL_TEXTURE_2D, courtTexID);
        gl.glUniform1i(shadeLoc, 1);
        uploadModelYRotation(gl, x, y, -1.2f, rotation, scale, scale, scale);

        gl.glBindVertexArray(vaoPlayingField[0]);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, vertNoPlayingField);
        gl.glBindVertexArray(0);
    }

    public void drawPowerUp(GL3 gl, PowerUp powerUp, float scale) {

        if (!powerUp.active) return;
        gl.glUniform1i(shadeLoc, 1);
        int tex = (powerUp.type == 1) ? powerUpFlashTex : powerUpGrowTex;
        gl.glBindTexture(GL.GL_TEXTURE_2D, tex);

        uploadModel(gl, powerUp.posX, powerUp.posY,-2.0f,0.0f, scale, scale, scale);

        gl.glBindVertexArray(vaoPowerUp[0]);
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, powerUpVertexCount);
        gl.glBindVertexArray(0);
    }


    @Override
    public void init(GLAutoDrawable d) {
        game = new Game();
        GL3 gl = d.getGL().getGL3();
        gl.glEnable(GL.GL_DEPTH_TEST);

        setupShaders(d);

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
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdBall[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn1.capacity() * Buffers.SIZEOF_FLOAT, dataIn1, GL3.GL_STATIC_DRAW);

        // VAO for ball
        gl.glGenVertexArrays(1, vaoBall, 0);
        gl.glBindVertexArray(vaoBall[0]);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdBall[0]);

        setupVAO(gl, vaoBall[0], vertBufIdBall[0], vertNoBall);

        gl.glBindVertexArray(0);

        // generating Player vertex VBO
        vertNoPlayer = playerVertexData.length / perVertexFloats;
        FloatBuffer dataIn2 = Buffers.newDirectFloatBuffer(playerVertexData.length);
        dataIn2.put(playerVertexData);
        dataIn2.flip();

        gl.glGenBuffers(1, vertBufIdPlayer, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdPlayer[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn2.capacity() *Buffers.SIZEOF_FLOAT, dataIn2, GL3.GL_STATIC_DRAW);

        // VAO for player
        gl.glGenVertexArrays(1, vaoPlayer, 0);
        gl.glBindVertexArray(vaoPlayer[0]);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdPlayer[0]);

        setupVAO(gl, vaoPlayer[0], vertBufIdPlayer[0], vertNoPlayer);

        gl.glBindVertexArray(0);

        // generating PlayingField vertex VBO
        vertNoPlayingField = playingFieldVertexData.length / perVertexFloats;
        FloatBuffer dataIn3 = Buffers.newDirectFloatBuffer(playingFieldVertexData.length);
        dataIn3.put(playingFieldVertexData);
        dataIn3.flip();

        gl.glGenBuffers(1, vertBufIdPlayingField, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdPlayingField[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn3.capacity() *Buffers.SIZEOF_FLOAT, dataIn3, GL3.GL_STATIC_DRAW);

        // VAO for playing field
        gl.glGenVertexArrays(1, vaoPlayingField, 0);
        gl.glBindVertexArray(vaoPlayingField[0]);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdPlayingField[0]);

        setupVAO(gl, vaoPlayingField[0], vertBufIdPlayingField[0], vertNoPlayingField);

        gl.glBindVertexArray(0);

        // generating ScoreZero vertex VBO
        vertNo_0 = scoreZeroVertexData.length / perVertexFloats;
        FloatBuffer dataIn4 = Buffers.newDirectFloatBuffer(scoreZeroVertexData.length);
        dataIn4.put(scoreZeroVertexData);
        dataIn4.flip();

        gl.glGenBuffers(1, vertBufId_0, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufId_0[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn4.capacity() *Buffers.SIZEOF_FLOAT, dataIn4, GL3.GL_STATIC_DRAW);

        // generating ScoreOne vertex VBO
        vertNo_1 = scoreOneVertexData.length / perVertexFloats;
        FloatBuffer dataIn5 = Buffers.newDirectFloatBuffer(scoreOneVertexData.length);
        dataIn5.put(scoreOneVertexData);
        dataIn5.flip();

        gl.glGenBuffers(1, vertBufId_1, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufId_1[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn5.capacity() *Buffers.SIZEOF_FLOAT, dataIn5, GL3.GL_STATIC_DRAW);

        // generating ScoreTwo vertex VBO
        vertNo_2 = scoreTwoVertexData.length / perVertexFloats;
        FloatBuffer dataIn6 = Buffers.newDirectFloatBuffer(scoreTwoVertexData.length);
        dataIn6.put(scoreTwoVertexData);
        dataIn6.flip();

        gl.glGenBuffers(1, vertBufId_2, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufId_2[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn6.capacity() *Buffers.SIZEOF_FLOAT, dataIn6, GL3.GL_STATIC_DRAW);

        // generating ScoreThree vertex VBO
        vertNo_3 = scoreThreeVertexData.length / perVertexFloats;
        FloatBuffer dataIn7 = Buffers.newDirectFloatBuffer(scoreThreeVertexData.length);
        dataIn7.put(scoreThreeVertexData);
        dataIn7.flip();

        gl.glGenBuffers(1, vertBufId_3, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufId_3[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn7.capacity() *Buffers.SIZEOF_FLOAT, dataIn7, GL3.GL_STATIC_DRAW);

        int[][] digitVBOs = {
                vertBufId_0,
                vertBufId_1,
                vertBufId_2,
                vertBufId_3
        };

        int[] digitVertCounts = { vertNo_0, vertNo_1, vertNo_2, vertNo_3 };

        for (int i = 0; i < 4; i++) {
            gl.glGenVertexArrays(1, vaoDigits, i);
            gl.glBindVertexArray(vaoDigits[i]);

            gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, digitVBOs[i][0]);

            setupVAO(gl, vaoDigits[i], digitVBOs[i][0], digitVertCounts[i]);

            gl.glBindVertexArray(0);
        }

        // generating PowerUp vertex VBO
        vertNoPowerUp = powerUpVertexData.length / perVertexFloats;
        FloatBuffer dataIn8 = Buffers.newDirectFloatBuffer(powerUpVertexData.length);
        dataIn8.put(powerUpVertexData);
        dataIn8.flip();

        gl.glGenBuffers(1, vertBufIdPowerUp, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdPowerUp[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, (long) dataIn8.capacity() *Buffers.SIZEOF_FLOAT, dataIn8, GL3.GL_STATIC_DRAW);

        gl.glGenVertexArrays(1, vaoPowerUp, 0);
        gl.glBindVertexArray(vaoPowerUp[0]);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertBufIdPowerUp[0]);

        setupVAO(gl, vaoPowerUp[0], vertBufIdPowerUp[0], vertNoPowerUp);

        gl.glBindVertexArray(0);

        powerUpVertexCount = vertNoPowerUp;
    }

    @Override
    public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
        GL3 gl = d.getGL().getGL3();
        gl.glViewport(0, 0, width, height);

        float aspect = (float) width / (float) height;

        float[] proj = makePerspective(60.0f, aspect, 1.0f, 5.0f);

        gl.glUseProgram(progID);
        gl.glUniformMatrix4fv(projectionLoc, 1, false, proj, 0);
        gl.glUseProgram(0);
    }

    private float[] inverseTranspose3x3(float[] m) {
        float[] inv = new float[9];

        // compute determinant
        float det = m[0]*(m[4]*m[8]-m[5]*m[7])
                - m[1]*(m[3]*m[8]-m[5]*m[6])
                + m[2]*(m[3]*m[7]-m[4]*m[6]);
        if (det == 0) return new float[]{1,0,0,0,1,0,0,0,1}; // fallback identity
        float invDet = 1.0f / det;

        // inverse
        inv[0] = (m[4]*m[8]-m[5]*m[7])*invDet;
        inv[1] = (m[2]*m[7]-m[1]*m[8])*invDet;
        inv[2] = (m[1]*m[5]-m[2]*m[4])*invDet;
        inv[3] = (m[5]*m[6]-m[3]*m[8])*invDet;
        inv[4] = (m[0]*m[8]-m[2]*m[6])*invDet;
        inv[5] = (m[2]*m[3]-m[0]*m[5])*invDet;
        inv[6] = (m[3]*m[7]-m[4]*m[6])*invDet;
        inv[7] = (m[1]*m[6]-m[0]*m[7])*invDet;
        inv[8] = (m[0]*m[4]-m[1]*m[3])*invDet;

        // transpose
        float[] nt = new float[9];
        nt[0] = inv[0]; nt[1] = inv[3]; nt[2] = inv[6];
        nt[3] = inv[1]; nt[4] = inv[4]; nt[5] = inv[7];
        nt[6] = inv[2]; nt[7] = inv[5]; nt[8] = inv[8];

        return nt;
    }


    @Override
    public void display (GLAutoDrawable d) {
        GL3 gl = d.getGL().getGL3();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glUseProgram(progID);
        gl.glUniform1i(modeLoc, MyGui2.renderer2.modeVal);

        switch (MyGui2.renderer2.modeVal) {
            case 1:
                gl.glUniform3f(lightDirLoc, 0.0f, -1.0f, 0.0f);
                break;
            case 2:
                gl.glUniform3f(lightDirLoc, 0.0f, 1.0f, 0.0f);
                break;
            case 3:
                gl.glUniform3f(lightDirLoc, -1.0f, -1.0f, 0.0f);
                break;
            case 4:
                gl.glUniform3f(lightDirLoc, - game.ball.posx, - game.ball.posy, 1.0f);
                break;
            case 5:
                gl.glUniform1f(metallicLoc, 0.0f);
                break;
            case 6:
                gl.glUniform1f(metallicLoc, 1.0f);
                break;
            case 7:
                gl.glUniform1f(roughnessLoc, 0.1f);
                break;
            case 8:
                gl.glUniform1f(roughnessLoc, 0.2f);
                break;
        }

        float[] modelviewMatrix = new float[16];
        for (int i = 0; i < 16; i++) modelviewMatrix[i] = 0f;
        modelviewMatrix[0] = 1; modelviewMatrix[5] = 1; modelviewMatrix[10] = 1; modelviewMatrix[15] = 1;

        gl.glUniformMatrix4fv(modelviewLoc, 1, false, modelviewMatrix, 0);

        gl.glActiveTexture(GL3.GL_TEXTURE0);
        gl.glUniform1i(texLoc, 0);

        float[] identityNormal = {
                1,0,0,
                0,1,0,
                0,0,1
        };

        gl.glUniformMatrix3fv(normalMatrixLoc, 1, false, identityNormal, 0);

        drawBall(gl, game.ball.posx, game.ball.posy, game.ball.rotation, 0.15f);
        drawBat(gl, game.player1, game.player1.posX, game.player1.posY, 270.0f, 1f);
        drawBat(gl, game.player2, game.player2.posX, game.player2.posY, 90.0f, 1f);

        gl.glBindTexture(GL.GL_TEXTURE_2D, courtTexID);
        drawPlayingField(gl, 0.0f, 0.0f, t, 2.0f);
        float offset = 0.01f;
        t += offset;

        drawPowerUp(gl, game.powerUp, 0.07f);

        switch (game.scoreP1) {
            case 0:
                drawDigit(gl, -0.1f, 1.0f, 0, vertNo_0);
                break;
            case 1:
                drawDigit(gl, -0.1f, 1.0f, 1, vertNo_1);
                break;
            case 2:
                drawDigit(gl, -0.1f, 1.0f, 2, vertNo_2);
                break;
            case 3:
                drawDigit(gl, -0.1f, 1.0f, 3, vertNo_3);
                break;
        }

        switch (game.scoreP2) {
            case 0:
                drawDigit(gl, 0.1f, 1.0f, 0, vertNo_0);
                break;
            case 1:
                drawDigit(gl, 0.1f, 1.0f, 1, vertNo_1);
                break;
            case 2:
                drawDigit(gl, 0.1f, 1.0f, 2, vertNo_2);
                break;
            case 3:
                drawDigit(gl, 0.1f, 1.0f, 3, vertNo_3);
                break;
        }
        game.step();
    }

    public void setupShaders(GLAutoDrawable d) {
        GL3 gl = d.getGL().getGL3(); // get the OpenGL 3 graphics context

        vertID = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        fragID = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);

        String[] vs = new String[]{
                """
        #version 150
        
        in vec3 inputPosition;
        in vec4 inputColor;
        in vec2 inputTexCoord;
        in vec3 inputNormal;
        
        uniform mat4 projection;
        uniform mat4 modelview;
        uniform mat3 normalMatrix;
        
        out vec3 vColor;
        out vec2 vTexCoord;
        out vec3 vNormal;
        out vec3 vViewPos;
        
        void main()
        {
            vColor = inputColor.rgb;
            vTexCoord = inputTexCoord;
            vNormal = normalMatrix * inputNormal;
            vec4 viewPos4 = modelview * vec4(inputPosition, 1.0);
            vViewPos = viewPos4.xyz;
            gl_Position = projection * viewPos4;
        }
        
        """
        };

        String[] fs = new String[]{
                """
         #version 150
        
         in vec3 vColor;
         in vec2 vTexCoord;
         in vec3 vNormal;
         in vec3 vViewPos;
         
         out vec4 outputColor;
         
         uniform sampler2D myTexture;
         uniform int shading;
         uniform vec3 lightDir;
         uniform int mode;
         
         uniform float metallic;
         uniform float roughness;
         
         const vec4 uLightColor = vec4(1.0, 1.0, 1.0, 1.0);
         const float uIrradiPerp = 5.0;
         
         
         #define RECIPROCAL_PI 0.3183098861837907
         
         vec3 rgb2lin(vec3 rgb) { // sRGB to linear approximation
           return pow(rgb, vec3(2.2));
         }
         
         vec3 lin2rgb(vec3 lin) { // linear to sRGB approximation
           return pow(lin, vec3(1.0 / 2.2));
         }
         
         vec3 fresnelSchlick(float cosTheta, vec3 F0) {
           return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
         }
         
         float D_GGX(float NoH, float roughness) {
           float alpha = roughness * roughness;
           float alpha2 = alpha * alpha;
           float NoH2 = NoH * NoH;
           float b = (NoH2 * (alpha2 - 1.0) + 1.0);
           return alpha2 * RECIPROCAL_PI / (b * b);
         }
         
         float G1_GGX_Schlick(float NoV, float roughness) {
           float alpha = roughness * roughness;
           float k = alpha / 2.0;
           return max(NoV, 0.001) / (NoV * (1.0 - k) + k);
         }
         
         float G_Smith(float NoV, float NoL, float roughness) {
           return G1_GGX_Schlick(NoL, roughness) * G1_GGX_Schlick(NoV, roughness);
         }
         
         float fresnelSchlick90(float cosTheta, float F0, float F90) {
            return F0 + (F90 - F0) * pow(1.0 - cosTheta, 5.0);
          }
          
          float disneyDiffuseFactor(float NoV, float NoL, float VoH, float roughness) {
            float alpha = roughness * roughness;
            float F90 = 0.5 + 2.0 * alpha * VoH * VoH;
            float F_in = fresnelSchlick90(NoL, 1.0, F90);
            float F_out = fresnelSchlick90(NoV, 1.0, F90);
            return F_in * F_out;
          }
          
          vec3 microfacetBRDF(in vec3 L, in vec3 V, in vec3 N,
                                  in float metallic, in float roughness, in vec3 baseColor, in float reflectance) {
          
            vec3 H = normalize(V + L);
          
            float NoV = clamp(dot(N, V), 0.0, 1.0);
            float NoL = clamp(dot(N, L), 0.0, 1.0);
            float NoH = clamp(dot(N, H), 0.0, 1.0);
            float VoH = clamp(dot(V, H), 0.0, 1.0);
          
            vec3 f0 = vec3(0.16 * (reflectance * reflectance));
            f0 = mix(f0, baseColor, metallic);
          
            vec3 F = fresnelSchlick(VoH, f0);
            float D = D_GGX(NoH, roughness);
            float G = G_Smith(NoV, NoL, roughness);
          
            vec3 spec = (F * D * G) / (4.0 * max(NoV, 0.001) * max(NoL, 0.001));
          
            vec3 rhoD = baseColor;
          
            // optionally
            rhoD *= vec3(1.0) - F;
            // rhoD *= disneyDiffuseFactor(NoV, NoL, VoH, roughness);
          
            rhoD *= (1.0 - metallic);
          
            vec3 diff = rhoD * RECIPROCAL_PI;
          
            return diff + spec;
          }
          
          void main() {
          
              vec3 baseColor = vColor;
              
              if (shading == 1) {
                               baseColor *= texture(myTexture, vTexCoord).rgb;
                           }
              
              vec3 L = normalize(-lightDir);
              vec3 V = normalize(-vViewPos);
              vec3 N = normalize(vNormal);
              
              float r = max(roughness, 0.04);
          
              vec3 radiance = vec3(0.0);
          
              float irradiance = max(dot(L, N), 0.0);
              if (irradiance > 0.0) {
                  vec3 brdf = microfacetBRDF(
                      L, V, N,
                      metallic,
                      r,
                      baseColor,  // baseColor placeholder
                      0.5 // reflectance placeholder
                  );
                  radiance += brdf * irradiance * uIrradiPerp; // ;
              }
          
              outputColor.rgb = lin2rgb(radiance);
              outputColor.a = 1.0;
          }
          
         
        """
        };


        gl.glShaderSource(vertID, 1, vs, null, 0);
        gl.glShaderSource(fragID, 1, fs, null, 0);

        // compile the shader
        gl.glCompileShader(vertID);
        gl.glCompileShader(fragID);

        // check for errors
        printShaderInfoLog(d, vertID);
        printShaderInfoLog(d, fragID);

        // create program and attach shaders
        progID = gl.glCreateProgram();
        gl.glAttachShader(progID, vertID);
        gl.glAttachShader(progID, fragID);

        // "outColor" is a user-provided OUT variable
        // of the fragment shader.
        // Its output is bound to the first color buffer
        // in the framebuffer
        gl.glBindFragDataLocation(progID, 0, "outputColor");

        // link the program
        gl.glLinkProgram(progID);
        // output error messages
        printProgramInfoLog(d, progID);

        // "inputPosition" and "inputColor" are user-provided
        // IN variables of the vertex shader.
        // Their locations are stored to be used later with
        // glEnableVertexAttribArray()
        vertexLoc = gl.glGetAttribLocation(progID, "inputPosition");
        colorLoc = gl.glGetAttribLocation(progID, "inputColor");
        texCoordLoc = gl.glGetAttribLocation(progID, "inputTexCoord");
        normalLoc = gl.glGetAttribLocation(progID, "inputNormal");

        // "projection" and "modelview" are user-provided
        // UNIFORM variables of the vertex shader.
        // Their locations are stored to be used later
        projectionLoc = gl.glGetUniformLocation(progID, "projection");
        modelviewLoc = gl.glGetUniformLocation(progID, "modelview");
        normalMatrixLoc =  gl.glGetUniformLocation(progID, "normalMatrix");
        texLoc = gl.glGetUniformLocation(progID, "myTexture");
        shadeLoc = gl.glGetUniformLocation(progID, "shading");
        lightDirLoc = gl.glGetUniformLocation(progID, "lightDir");
        modeLoc = gl.glGetUniformLocation(progID, "mode");
        metallicLoc = gl.glGetUniformLocation(progID, "metallic");
        roughnessLoc = gl.glGetUniformLocation(progID, "roughness");
    }

    private void printShaderInfoLog(GLAutoDrawable d, int obj) {
        GL3 gl = d.getGL().getGL3(); // get the OpenGL 3 graphics context
        IntBuffer infoLogLengthBuf = IntBuffer.allocate(1);
        int infoLogLength;
        gl.glGetShaderiv(obj, GL3.GL_INFO_LOG_LENGTH, infoLogLengthBuf);
        infoLogLength = infoLogLengthBuf.get(0);
        if (infoLogLength > 0) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(infoLogLength);
            gl.glGetShaderInfoLog(obj, infoLogLength, infoLogLengthBuf, byteBuffer);
            for (byte b : byteBuffer.array()) {
                System.err.print((char) b);
            }
        }
    }


    private void printProgramInfoLog(GLAutoDrawable d, int obj) {
        GL3 gl = d.getGL().getGL3(); // get the OpenGL 3 graphics context
        IntBuffer infoLogLengthBuf = IntBuffer.allocate(1);
        int infoLogLength;
        gl.glGetProgramiv(obj, GL3.GL_INFO_LOG_LENGTH, infoLogLengthBuf);
        infoLogLength = infoLogLengthBuf.get(0);
        if (infoLogLength > 0) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(infoLogLength);
            gl.glGetProgramInfoLog(obj, infoLogLength, infoLogLengthBuf, byteBuffer);
            for (byte b : byteBuffer.array()) {
                System.err.print((char) b);
            }
        }
    }

    private void setupVAO(GL3 gl, int vaoId, int vboId, int vertexCount) {
        gl.glBindVertexArray(vaoId);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboId);

        if (vertexLoc >= 0) {
            gl.glEnableVertexAttribArray(vertexLoc);
            gl.glVertexAttribPointer(vertexLoc, 3, GL3.GL_FLOAT, false, STRIDE, 0);
        }

        long colorOffset = 3 * Buffers.SIZEOF_FLOAT;
        if (colorLoc >= 0) {
            gl.glEnableVertexAttribArray(colorLoc);
            gl.glVertexAttribPointer(colorLoc, 4, GL3.GL_FLOAT, false, STRIDE, colorOffset);
        }

        long texOffset = (3 + 4) * Buffers.SIZEOF_FLOAT;
        if (texCoordLoc >= 0) {
            gl.glEnableVertexAttribArray(texCoordLoc);
            gl.glVertexAttribPointer(texCoordLoc, 2, GL3.GL_FLOAT, false, STRIDE, texOffset);
        }

        long normalOffset = (3 + 4 + 2) * Buffers.SIZEOF_FLOAT;
        if (normalLoc >= 0) {
            gl.glEnableVertexAttribArray(normalLoc);
            gl.glVertexAttribPointer(normalLoc, 3, GL3.GL_FLOAT, false, STRIDE, normalOffset);
        }

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        gl.glBindVertexArray(0);
    }

    private float[] makePerspective(float fovyDeg, float aspect, float zNear, float zFar) {
        float fovy = (float)Math.toRadians(fovyDeg);
        float f = (float)(1.0 / Math.tan(fovy / 2.0));
        float[] m = new float[16];
        for (int i = 0; i < 16; i++) m[i] = 0;
        m[0] = f / aspect;
        m[5] = f;
        m[10] = (zFar + zNear) / (zNear - zFar);
        m[11] = -1f;
        m[14] = (2f * zFar * zNear) / (zNear - zFar);
        return m;
    }

    private void uploadModel(GL3 gl, float x, float y, float z, float rotationDeg, float sx, float sy, float sz) {

        float[] m = new float[16];

        float r = (float) Math.toRadians(rotationDeg);
        float c = (float) Math.cos(r);
        float s = (float) Math.sin(r);

        m[0]  = c * sx;   m[4]  = -s * sy;  m[8]  = 0;   m[12] = x;
        m[1]  = s * sx;   m[5]  =  c * sy;  m[9]  = 0;   m[13] = y;
        m[2]  = 0;        m[6]  = 0;        m[10] = sz;  m[14] = z;
        m[3]  = 0;        m[7]  = 0;        m[11] = 0;   m[15] = 1;

        float[] mv3 = new float[9];
        mv3[0] = m[0]; mv3[1] = m[4]; mv3[2] = m[8];
        mv3[3] = m[1]; mv3[4] = m[5]; mv3[5] = m[9];
        mv3[6] = m[2]; mv3[7] = m[6]; mv3[8] = m[10];

        float[] normalMat = inverseTranspose3x3(mv3);

        gl.glUniformMatrix3fv(normalMatrixLoc, 1, false, normalMat, 0);
        gl.glUniformMatrix4fv(modelviewLoc, 1, false, m, 0);
    }

    private void uploadModelYRotation(GL3 gl, float x, float y, float z, float rotationDeg, float sx, float sy, float sz) {

        float[] m = new float[16];

        float r = (float) Math.toRadians(rotationDeg);
        float c = (float) Math.cos(r);
        float s = (float) Math.sin(r);

        m[0]  = c * sx;  m[4]  = 0;  m[8]  = s * sz;   m[12] = x;
        m[1]  = 0;       m[5]  = sy; m[9]  = 0;       m[13] = y;
        m[2]  = -s * sx; m[6]  = 0;  m[10] = c * sz;  m[14] = z;
        m[3]  = 0;       m[7]  = 0;  m[11] = 0;      m[15] = 1;

        float[] mv3 = new float[9];
        mv3[0] = m[0]; mv3[1] = m[4]; mv3[2] = m[8];
        mv3[3] = m[1]; mv3[4] = m[5]; mv3[5] = m[9];
        mv3[6] = m[2]; mv3[7] = m[6]; mv3[8] = m[10];

        float[] normalMat = inverseTranspose3x3(mv3);

        gl.glUniformMatrix3fv(normalMatrixLoc, 1, false, normalMat, 0);
        gl.glUniformMatrix4fv(modelviewLoc, 1, false, m, 0);
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
        GL3 gl = d.getGL().getGL3(); // get the OpenGL 2 graphics context

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
            gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);

            //request textureID
            final int[] textureID = new int[1];
            gl.glGenTextures( 1, textureID, 0);

            // bind texture
            gl.glBindTexture(GL3.GL_TEXTURE_2D, textureID[0]);

            //define how to filter the texture (important but ignore for now)
            gl.glTexParameteri (GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
            gl.glTexParameteri (GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);

            //texture colors should replace the original color values
            //gl.glTexEnvf(GL3.GL_TEXTURE_ENV, GL3.GL_TEXTURE_ENV_MODE, GL3.GL_REPLACE); //GL_MODULATE

            // specify the 2D texture map
            gl.glTexImage2D(GL3.GL_TEXTURE_2D, level, GL3.GL_RGB, width, height, border, GL3.GL_RGBA, GL3.GL_UNSIGNED_BYTE, buffer);

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

    public static Renderer2 renderer2;

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

        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                switch (event.getKeyCode()) {
                    case '1': renderer2.modeVal = 1; break;
                    case '2': renderer2.modeVal = 2; break;
                    case '3': renderer2.modeVal = 3; break;
                    case '4': renderer2.modeVal = 4; break;
                }
            }
        });


    }

}

class Game {

    public Ball ball = new Ball(0, 0, 0.6f, 0.2f);
    public Player player1 = new Player(-1.8f, 0);
    public Player player2 = new Player(1.8f, 0);

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
            if (ball.posx >= 2.0f) {
                scoreP1++;
                resetBall(-0.6f);
                spawnPowerUpIfNeeded();
            } else if (ball.posx <= -2.0f) {
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
