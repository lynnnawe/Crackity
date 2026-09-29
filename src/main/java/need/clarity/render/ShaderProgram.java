package need.clarity.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

public class ShaderProgram {
   private final int intA;
   private final String stringB;
   private final String stringC;
   private final Map<String, Integer> mapD;

   public ShaderProgram(String string, String string1) {
      HashMap hashMap = new HashMap();
      this.mapD = hashMap;
      String string2 = "/assets/clarity/shaders/" + string + "/" + string1 + ".vsh";
      this.stringB = string2;
      String string3 = "/assets/clarity/shaders/" + string + "/" + string1 + ".fsh";
      this.stringC = string3;
      String string4 = this.stringB;
      int i = this.methodB(string4, 35633);
      String string5 = this.stringC;
      int j = this.methodB(string5, 35632);
      int k = GL20.glCreateProgram();
      this.intA = k;
      GL20.glAttachShader(this.intA, i);
      GL20.glAttachShader(this.intA, j);
      GL20.glLinkProgram(this.intA);
      if (GL20.glGetProgrami(this.intA, 35714) == 0) {
         String string6 = "Failed to link shader program: " + GL20.glGetProgramInfoLog(this.intA);
         RuntimeException runtimeException = new RuntimeException(string6);
         throw runtimeException;
      }

      GL20.glDeleteShader(i);
      GL20.glDeleteShader(j);
   }

   private int methodB(String string, int i) {
      try {
         String string1 = this.methodA(string);
         int j = GL20.glCreateShader(i);
         GL20.glShaderSource(j, string1);
         GL20.glCompileShader(j);
         if (GL20.glGetShaderi(j, 35713) == 0) {
            String string4 = GL20.glGetShaderInfoLog(j);
            String string3 = "Failed to compile shader: " + string + "\n" + string4;
            RuntimeException runtimeException1 = new RuntimeException(string3);
            throw runtimeException1;
         } else {
            return j;
         }
      } catch (IOException iOException) {
         String string2 = "Failed to load shader: " + string;
         RuntimeException runtimeException = new RuntimeException(string2, iOException);
         throw runtimeException;
      }
   }

   private String methodA(String string) {
      InputStream inputStream = ShaderProgram.class.getResourceAsStream(string);
      if (inputStream == null) {
         String string2 = "Shader file not found: " + string;
         IOException iOException = new IOException(string2);
         throw iOException;
      }

      StringBuilder stringBuilder = new StringBuilder();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      while (true) {
         try {
            String string1 = bufferedReader.readLine();
            if (string1 == null) {
               break;
            }

            stringBuilder.append(string1).append("\n");
         } catch (Throwable throwable1) {
            try {
               bufferedReader.close();
            } catch (Throwable throwable) {
               throwable1.addSuppressed(throwable);
            }

            throw throwable1;
         }
      }

      bufferedReader.close();
      return stringBuilder.toString();
   }

   public void methodA() {
      GL20.glUseProgram(this.intA);
   }

   public void methodB() {
      GL20.glUseProgram(0);
   }

   private int methodB(String string) {
      Integer integer = this.mapD.get(string);
      if (integer != null) {
         return integer;
      }

      int i = GL20.glGetUniformLocation(this.intA, string);
      Map map = this.mapD;
      Integer integer1 = i;
      map.put(string, integer1);
      return i;
   }

   public void methodA(String string, int i) {
      int j = this.methodB(string);
      if (j != -1) {
         GL20.glUniform1i(j, i);
      }
   }

   public void methodA(String string, float f) {
      int i = this.methodB(string);
      if (i != -1) {
         GL20.glUniform1f(i, f);
      }
   }

   public void methodA(String string, float f, float g, float h) {
      int i = this.methodB(string);
      if (i != -1) {
         GL20.glUniform3f(i, f, g, h);
      }
   }

   public void methodA(String string, Vector3f vector3f) {
      float f = vector3f.x;
      float g = vector3f.y;
      float h = vector3f.z;
      this.methodA(string, f, g, h);
   }

   public void methodA(String string, boolean bl) {
      byte b;
      if (bl) {
         b = 1;
      } else {
         b = 0;
      }

      this.methodA(string, b);
   }
}
