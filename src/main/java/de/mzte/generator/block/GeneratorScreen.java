package de.mzte.generator.block;

import com.mojang.blaze3d.platform.GlStateManager;
import de.mzte.generator.Generator;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GeneratorScreen extends ContainerScreen<GeneratorContainer> {
	private GeneratorContainer container;
	private int relY;
	private int relX;
	private ResourceLocation GUI = new ResourceLocation(Generator.MODID,"textures/gui/generator.png");

	public GeneratorScreen(GeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.container = screenContainer;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		//xSize = 175;
		//ySize = 165;

		GlStateManager.color4f(1F, 1F, 1F, 1F);
		this.minecraft.getTextureManager().bindTexture(GUI);
		int relX = (this.width - this.xSize) / 2;
		int relY = (this.height - this.ySize) / 2;
		this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
		drawBurnTime(((GeneratorTile)container.tile).getBurnTimeRelative());
		drawEnergyMeter(((GeneratorTile)container.tile).getEnergyRelative());
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
		if(this.isPointInRegion(156, 12, 12, 56, mouseX, mouseY)) {
			renderEnergyMeterTooltip(mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 15, 10, 0x8b8b8b);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 15, 70,0x8b8b8b);
	}

	private void drawBurnTime(int relBurnTime) {
		if(relBurnTime != 0) {
			int width = 14;
			int relheight = 1 + relBurnTime;
			int xpos = 176;
			int ypos = 13 - relBurnTime;
			int xTarget = this.guiLeft + 82;
			int yTarget = (this.guiTop + 60) - relBurnTime;
			this.blit(xTarget, yTarget, xpos, ypos, width, (int)relheight);
		}
	}

	private void drawEnergyMeter(int relEnergy) {
		if(relEnergy != 0) {
			int width = 12;
			int relheight = 1 + relEnergy;
			int xpos = 190;
			int ypos = 55 - relEnergy;
			int xTarget = this.guiLeft + 156;
			int yTarget = (this.guiTop + 68) - relEnergy;
			this.blit(xTarget, yTarget, xpos, ypos, width, (int) relheight);
		}
	}

	private void renderEnergyMeterTooltip(int x, int y) {
		this.renderTooltip(I18n.format("tooltip.mztegenerator.generatorEnergy", container.getEnergy(), container.getMaxEnergy()), x, y);
	}

}
