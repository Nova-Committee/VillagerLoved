package committee.nova.villagerloved;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;

@Mod(VillagerLoved.MODID)
public class VillagerLoved {
    public static final String MODID = "villagerloved";

    public static TagKey<Item> VILLAGER_LOVED = ItemTags.create(new ResourceLocation(MODID, "villager_loved"));
}
