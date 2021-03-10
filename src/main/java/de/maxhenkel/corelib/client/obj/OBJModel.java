package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import de.maxhenkel.corelib.client.RenderUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class OBJModel {

    private ResourceLocation model;

    private OBJModelData data;

    public OBJModel(ResourceLocation model) {
        this.model = model;
    }

    @OnlyIn(Dist.CLIENT)
    private void load() {
        if (data == null) {
            data = OBJLoader.load(model);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void render(ResourceLocation texture, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
        load();
        matrixStack.pushPose();

        IVertexBuilder builder = buffer.getBuffer(getRenderType(texture, true));

        for (int i = 0; i < data.faces.size(); i++) {
            int[][] face = data.faces.get(i);
            RenderUtils.vertex(builder, matrixStack, data.positions.get(face[0][0]), data.texCoords.get(face[0][1]), data.normals.get(face[0][2]), light, OverlayTexture.NO_OVERLAY);
            RenderUtils.vertex(builder, matrixStack, data.positions.get(face[1][0]), data.texCoords.get(face[1][1]), data.normals.get(face[1][2]), light, OverlayTexture.NO_OVERLAY);
            RenderUtils.vertex(builder, matrixStack, data.positions.get(face[2][0]), data.texCoords.get(face[2][1]), data.normals.get(face[2][2]), light, OverlayTexture.NO_OVERLAY);
        }
        matrixStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    private static RenderType getRenderType(ResourceLocation resourceLocation, boolean culling) {
        RenderType.State state = RenderType.State
                .builder()
                .setTextureState(new RenderState.TextureState(resourceLocation, false, false))
                .setTransparencyState(new RenderState.TransparencyState("no_transparency", RenderSystem::disableBlend, () -> {
                }))
                .setDiffuseLightingState(new RenderState.DiffuseLightingState(false))
                .setAlphaState(new RenderState.AlphaState(0.003921569F))
                .setLightmapState(new RenderState.LightmapState(true))
                .setOverlayState(new RenderState.OverlayState(true))
                .setCullState(new RenderState.CullState(culling))
                .createCompositeState(true);
        return RenderType.create("entity_cutout", DefaultVertexFormats.NEW_ENTITY, GL11.GL_TRIANGLES, 256, true, false, state);
    }

    static class OBJModelData {
        private List<Vector3f> positions;
        private List<Vector2f> texCoords;
        private List<Vector3f> normals;
        private List<int[][]> faces;

        public OBJModelData(List<Vector3f> positions, List<Vector2f> texCoords, List<Vector3f> normals, List<int[][]> faces) {
            this.positions = positions;
            this.texCoords = texCoords;
            this.normals = normals;
            this.faces = faces;
        }

        public List<Vector3f> getPositions() {
            return positions;
        }

        public List<Vector2f> getTexCoords() {
            return texCoords;
        }

        public List<Vector3f> getNormals() {
            return normals;
        }

        public List<int[][]> getFaces() {
            return faces;
        }
    }

}
