package committee.nova.villagerloved.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import committee.nova.villagerloved.VillagerLoved;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(Villager.class)
public class MixinVillager {
    @Unique
    private static boolean villagerloved$initialized;

    @Mutable
    @Shadow
    @Final
    private static Set<Item> WANTED_ITEMS;

    @Mutable
    @Shadow
    @Final
    public static Map<Item, Integer> FOOD_POINTS;

    @Unique
    private static void villagerloved$initVillagerLoved() {
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

    @Redirect(method = "wantsToPickUp", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", remap = false))
    private boolean redirect$wantsToPickup(Set<Item> instance, Object o) {
        if (!villagerloved$initialized) {
            villagerloved$initVillagerLoved();
            villagerloved$initialized = true;
        }
        return WANTED_ITEMS.contains((Item) o);
    }

    @Redirect(method = "eatUntilFull", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object redirect$eatUntilFull(Map<Item, Integer> instance, Object o) {
        if (!villagerloved$initialized) {
            villagerloved$initVillagerLoved();
            villagerloved$initialized = true;
        }
        return FOOD_POINTS.get((Item) o);
    }
}
