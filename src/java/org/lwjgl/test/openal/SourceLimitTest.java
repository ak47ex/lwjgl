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
 * * Neither the name of 'Light Weight Java Game Library' nor the names of
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
package org.lwjgl.test.openal;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.OpenALException;

import java.nio.IntBuffer;

/**
 * $Id$
 *
 * Simple test for testing the number of available sources
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 */
public class SourceLimitTest extends BasicTest {

	/** Sources to create */
	protected int sourcesToCreate = 64;

	/**
	 * Creates an instance of SourceLimitTest
	 */
	public SourceLimitTest() {
		super();
	}

	/**
	 * Runs the actual test, using supplied arguments
	 */
	protected void execute(String[] args) {
		//parse 1st arg to sourcecount
		if (args.length > 0) {
			try {
				sourcesToCreate = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				System.out.println(
					"Unable to parse parameter to integer. Defaulting to 64 sources.");
			}
		}

    System.out.print("Creating " + sourcesToCreate + " in one go...");
    try {
      CreateAllSources();
    } catch(OpenALException oale) {
    }

    
    System.out.print("Creating " + sourcesToCreate + " one at a time...");
    try {
      CreateSourcesStep();
    } catch(Exception e) {
    }
		//shutdown
		alExit();
	}

	/**
	 * Tests the creation of n sources in on go
	 */
	protected void CreateAllSources() {
		int lastError;

		//make bytbuffer that can hold sourcesToCreate sources
		IntBuffer sources = createIntBuffer(sourcesToCreate);

		//Create sourcesToCreate sources in one fell swoop
		AL.alGenSources(sourcesToCreate, sources);
		if ((lastError = AL.alGetError()) != AL.AL_NO_ERROR) {
			System.out.println("failed to create " + sourcesToCreate + " sources (" + AL.alGetString(lastError) + ")");
			return;
		}

		//delete sources
		AL.alDeleteSources(sourcesToCreate, sources);

		System.out.println("created " + sourcesToCreate + " sources successfully!");
	}

	/**
	 * Tests if n sources can be created one at a time
	 */
	protected void CreateSourcesStep() {
		int lastError;
		int sourcesCreated = 0;

		//make bytbuffer that can hold sourcesToCreate sources
		IntBuffer[] sources = new IntBuffer[sourcesToCreate];

    //create the sources
		for (int i = 0; i <= sourcesToCreate; i++) {
      sources[i] = createIntBuffer(1);
			AL.alGenSources(1, sources[i]);
			if ((lastError = AL.alGetError()) != AL.AL_NO_ERROR) {
				System.out.println("failed to create source: " + (i + 1));
				break;
			}
      sourcesCreated++;
		}
    
    //delete allocated sources
		for (int i = 0; i < sourcesCreated; i++) {
			//delete buffers and sources
			AL.alDeleteSources(1, sources[i]);
			if ((lastError = AL.alGetError()) != AL.AL_NO_ERROR) {
				System.out.println("failed to delete source: " + i + "(" + AL.alGetString(lastError) + ")");
				break;
			}
		}
    
    if(sourcesCreated != sourcesToCreate) {
      System.out.println("created " + sourcesCreated + " sources before failing");
    } else {
      System.out.println("created " + sourcesCreated + " sources successfully!");
    }
	}

	/**
	 * main entry point
	 *
	 * @param args String array containing arguments
	 */
	public static void main(String[] args) {
		SourceLimitTest sourceLimitTest = new SourceLimitTest();
		sourceLimitTest.execute(args);
	}
}