package committee.nova.villagerloved.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import committee.nova.villagerloved.VillagerLoved;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(Villager.class)
public class MixinVillager {
    @Mutable
    @Shadow
    @Final
    private static Set<Item> WANTED_ITEMS;

    @Mutable
    @Shadow
    @Final
    public static Map<Item, Integer> FOOD_POINTS;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void inject$clinit(CallbackInfo ci) {
        final List<Item> villagerLoved = ForgeRegistries.ITEMS.getValues()
                .stream()
                .filter(i -> i.getDefaultInstance().is(VillagerLoved.VILLAGER_LOVED))
                .toList();
        final Set<Item> wanted = Sets.newHashSet(WANTED_ITEMS);
        wanted.addAll(villagerLoved);
        WANTED_ITEMS = wanted;
        final Map<Item, Integer> foodPoints = Maps.newHashMap(FOOD_POINTS);
        villagerLoved
                .stream()
                .filter(Item::isEdible)
                .forEach(i -> foodPoints.put(i, i.getFoodProperties().getNutrition()));
        FOOD_POINTS = foodPoints;
    }
}
