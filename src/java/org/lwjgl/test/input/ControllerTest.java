/*
 * Copyright (c) 2002 Lightweight Java Game Library Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'Lightweight Java Game Library' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.test.input;

import org.lwjgl.DisplayMode;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLU;
import org.lwjgl.vector.Vector2f;

/**
 * $Id$
 * <br>
 * Controller test
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 */
public class ControllerTest {

  /** OpenGL instance */
  private GL gl;

  /** GLU instance */
  private GLU glu;

  /** position of quad to draw */
  private Vector2f position = new Vector2f(320.0f, 240.0f);
  
  /** Display mode selected */
  private DisplayMode displayMode;

  /** Creates a new instance of ControllerTest */
  public ControllerTest() {
  }

  private void initialize() {
    // create display and opengl
    setupDisplay(false);

    try {
      Keyboard.create();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
  
  private void setupDisplay(boolean fullscreen) {
    try {
      gl = new GL("ControllerTest", 50, 50, 640, 480, 16, 0, 0, 0);
      gl.create();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }

    initializeOpenGL();    
  }

  private void initializeOpenGL() {
    GL.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GLU.gluOrtho2D(0.0, 640, 0, 480);
  }

  public void executeTest() {
    initialize();

    createController();

    wiggleController();

    Controller.destroy();
    Keyboard.destroy();
    gl.destroy();
  }

  private void createController() {
    try {
      Controller.create();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void wiggleController() {
    while (!gl.isCloseRequested()) {
      gl.tick();
      
      if(gl.isMinimized()) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException inte) {
          inte.printStackTrace();
        }
        continue;
      }

      Controller.poll();
      Keyboard.poll();
      
      if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
        return;
      }

      if (Controller.x > 200) {
        position.x += 1;
      }
        
      if (Controller.x < -200) {
        position.x -= 1;
      }
        
      if (Controller.y < -200) {
        position.y += 1;
      }
      
      if (Controller.y > 200) {
        position.y -= 1;
      }
      
      if(position.x<0) {
        position.x = 0;
      } else if (position.x>640-60) {
        position.x = 640-60;
      }
      
      if(position.y < 0) {
        position.y = 0;
      } else if (position.y>480-30) {
        position.y = 480-30;
      }
      

      render();

      gl.paint();
    }
  }
  
  private void render() {
    GL.glClear(GL.GL_COLOR_BUFFER_BIT);

    GL.glBegin(GL.GL_POLYGON);
    {
      float color = 1.0f;
      int buttonDown = 0;
      
      for(int i=0;i<Controller.buttonCount; i++) {
        if(Controller.isButtonDown(i)) {
          color = (1.0f / Controller.buttonCount) * (i+1);
          System.out.println("Button " + i + " down"); 
        }
      }
      GL.glColor3f(color, color, color);
      
      GL.glVertex2f(position.x + 0.0f, position.y + 0.0f);
      GL.glVertex2f(position.x + 0.0f, position.y + 30.0f);
      GL.glVertex2f(position.x + 40.0f, position.y + 30.0f);
      GL.glVertex2f(position.x + 60.0f, position.y + 15.f);
      GL.glVertex2f(position.x + 40.0f, position.y + 0.0f);
    }
    GL.glEnd();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ControllerTest ct = new ControllerTest();
    ct.executeTest();
  }
}
