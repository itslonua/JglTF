/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2017 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.model.creation;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import de.javagl.jgltf.model.AccessorDatas;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.Accessors;
import de.javagl.jgltf.model.ElementType;
import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.model.impl.DefaultAccessorModel;
import de.javagl.jgltf.model.io.Buffers;

/**
 * Methods to create to {@link AccessorModel} instances
 */
public class AccessorModels 
{
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_INT</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedIntScalar(IntBuffer data)
    {
        return create(
            GltfConstants.GL_UNSIGNED_INT, "SCALAR",
            Buffers.createByteBufferFrom(data));
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_SHORT</code> and the type <code>"SCALAR"</code>.<br>
     * <br>
     * The elements of the given buffer will be cast to <code>short</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedShortScalar(IntBuffer data)
    {
        return create(  
            GltfConstants.GL_UNSIGNED_SHORT, "SCALAR",
            Buffers.castToShortByteBuffer(data));
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_UNSIGNED_SHORT</code> and the type <code>"SCALAR"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createUnsignedShortScalar(
        ShortBuffer data)
    {
        return create(  
            GltfConstants.GL_UNSIGNED_SHORT, "SCALAR",
            Buffers.createByteBufferFrom(data));
    }

    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_FLOAT</code> and the type <code>"VEC2"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createFloat2D(FloatBuffer data)
    {
        return create(
            GltfConstants.GL_FLOAT, "VEC2", 
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_FLOAT</code> and the type <code>"VEC3"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createFloat3D(FloatBuffer data)
    {
        return create(
            GltfConstants.GL_FLOAT, "VEC3", 
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Creates a new {@link AccessorModel} with the component type
     * <code>GL_FLOAT</code> and the type <code>"VEC4"</code>.
     * 
     * @param data The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * (in bytes!) is not divisible by the element size that is implied by 
     * the type and component type
     */
    public static DefaultAccessorModel createFloat4D(FloatBuffer data)
    {
        return create(
            GltfConstants.GL_FLOAT, "VEC4", 
            Buffers.createByteBufferFrom(data));
    }
    
    /**
     * Create an {@link AccessorModel} from the given data
     * 
     * @param componentType The component type, as a GL constant
     * @param type The type of the data, as a string corresponding to
     * the {@link ElementType} of the accessor
     * @param byteBuffer The actual data
     * @return The {@link AccessorModel}
     * @throws IllegalArgumentException If the capacity of the given buffer
     * is not divisible by the element size that is implied by the given
     * type and component type
     */
    static DefaultAccessorModel create(
        int componentType, String type, ByteBuffer byteBuffer)
    {
        ElementType elementType = ElementType.valueOf(type);
        int numComponents = elementType.getNumComponents();
        int numBytesPerComponent = 
            Accessors.getNumBytesForAccessorComponentType(componentType);
        int numBytesPerElement = numComponents * numBytesPerComponent;
        if (byteBuffer.capacity() % numBytesPerElement != 0)
        {
            throw new IllegalArgumentException(
                "Invalid data for type " + type + " accessor with "
                + Accessors.getDataTypeForAccessorComponentType(componentType)
                + "components: The data length is " + byteBuffer.capacity() 
                + " which is not divisble by " + numBytesPerElement);
        }
        int count = byteBuffer.capacity() / numBytesPerElement;
        DefaultAccessorModel accessorModel = new DefaultAccessorModel(
            componentType, count, elementType);
        accessorModel.setAccessorData(
            AccessorDatas.create(accessorModel, byteBuffer));
        return accessorModel;
    }
    
    /**
     * Compute the number of bytes that the given {@link AccessorModel} data
     * has to be aligned to. 
     * 
     * @param accessorModel The {@link AccessorModel}
     * @return The alignment bytes
     */
    static int computeAlignmentBytes(AccessorModel accessorModel)
    {
        return accessorModel.getComponentSizeInBytes();
    }
    
    /**
     * Compute the alignment for the given {@link AccessorModel} instances.
     * This is the least common multiple of the alignment of all instances.
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The alignment
     */
    static int computeAlignmentBytes(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int alignmentBytes = 1;
        for (AccessorModel accessorModel : accessorModels)
        {
            alignmentBytes = Utils.computeLeastCommonMultiple(alignmentBytes, 
                AccessorModels.computeAlignmentBytes(accessorModel));
        }
        return alignmentBytes;
    }
    
    /**
     * Compute the byte stride that is common for the given 
     * {@link AccessorModel} instances. This is the maximum of the
     * {@link AccessorModel#getElementSizeInBytes() element sizes}
     * of the given models.
     * 
     * @param accessorModels The {@link AccessorModel} instances
     * @return The common byte stride
     */
    static int computeCommonByteStride(
        Iterable<? extends AccessorModel> accessorModels)
    {
        int commonByteStride = 1;
        for (AccessorModel accessorModel : accessorModels)
        {
            int elementSize = accessorModel.getElementSizeInBytes();
            commonByteStride = Math.max(commonByteStride, elementSize);
        }
        return commonByteStride;
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private AccessorModels()
    {
        // Private constructor to prevent instantiation
    }
    
    
}