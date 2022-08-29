package com.adityagupta.arcompanion.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.adityagupta.arcompanion.R
import com.adityagupta.arcompanion.adapters.CustomAdapter
import com.adityagupta.arcompanion.databinding.ActivityArsupportedBinding

class ARSupportedActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityArsupportedBinding
    var modelList = arrayOf<String>("ambulance","apple","apple-half","avocado","avocado-half","axe","bacon","bacon-raw","bag","bag-flat","banana","barn","barrel","bed","beet","bench","bike","blaster-a","boat-large","boat-small","bottle","bottle-ketchup","bottle-large","bottle-musterd","bottle-oil","bowl","bowl-broth","bowl-cereal","bowl-soup","bread","bridge-01","broccoli","bunny","burger","burger-cheese","burger-cheese-double","burger-double","cabbage","cactus","cake","cake-birthday","cake-slicer","can","can-open","can-small","candy-bar","candy-bar-wrapper","cannon","cannon-ball","cannon-large","cannon-mobile","carrot","carton","carton-small","cauliflower","celery-stick","cheese","cheese-cut","cheese-slicer","cherries","chest","chinese","chocolate","chocolate-wrapper","chopstick","chopstick-fancy","citroen-old-van","cocktail","coconut","coconut-half","cookie","cookie-chocolate","cooking-fork","cooking-knife","cooking-knife-choppi","cooking-spatula","cooking-spoon","corn","corn-dog","coronavirus","croissant","cup","cup-saucer","cup-tea","cup-thea","cupcake","cutting-board","cutting-board-japane","cutting-board-round","cyborg-female","delivery-truck","dim-sum","dogue","donut","donut-1","donut-chocolate","donut-sprinkles","dragon","egg","egg-cooked","egg-cup","egg-half","eggplant","expresso-cup","ferris-wheel","firetruck","fish","fish-bones","formation-large-rock","formation-large-ston","formation-rock","formation-stone","frappe","fries","fries-empty","frikandel-speciaal","frying-pan","frying-pan-lid","garbage-truck","ginger-bread","ginger-bread-cutter","glass","glass-wine","grapes","guitar","hatchback","headphones","helicopter","hole","honey","hot-dog","hot-dog-raw","house-18","house-3","house-4","house-5","house-6","house-7","house1","ice-cream","ice-cream-cone","ice-cream-cup","ice-cream-scoop","ice-cream-scoop-mint","ice-cream-truck","knife-block","leek","lemon","lemon-half","les-paul","library-large","loaf","loaf-baguette","loaf-round","lollypop","low-poly-farm","low-poly-horse","low-poly-tree","lucy","maki-roe","maki-salmon","maki-vegetable","male","meat-cooked","meat-patty","meat-raw","meat-ribs","meat-sausage","meat-tenderizer","mechanical-keyboard","mechanical-keyboard-","mincemeat-pie","mortar","mortar-pestle","muffin","mug","mug-1","mushroom","mushroom-half","mussel","mussel-open","nes","nes-controller","node","onion","onion-half","orange","paddle","palm-detailed-long","palm-detailed-short","palm-long","palm-short","pan","pan-stew","pancakes","paprika","paprika-slice","peanut-butter","pear","pear-half","pepper","pepper-mill","pie","pillow","pineapple","pirate-captain","pirate-crew","pirate-officer","pizza","pizza-box","pizza-cutter","plant","plant-pirate-kit","plate","plate-broken","plate-deep","plate-dinner","plate-rectangle","plate-sauerkraut","police-car","popsicle","popsicle-chocolate","popsicle-stick","pot","pot-lid","pot-stew","pot-stew-lid","present","pudding","pumpkin","pumpkin-basic","race-car","race-futrure","radish","react-logo","rice-ball","rolling-pin","salad","sandwich","sausage","sausage-half","sci-fi-crate","sedan","shaker-pepper","shaker-salt","ship-dark","ship-light","ship-wreck","shovel","skater-female","skater-male","skewer","skewer-vegetables","soda","soda-bottle","soda-can","soda-can-crushed","soda-glass","soy","spilling-coffee","sports-sedan","steamer","strawberry","styrofoam","styrofoam-dinner","sub","sundae","survivor-female","survivor-male","sushi-egg","sushi-roll","sushi-salmon","suv","suv-luxury","suzanne-high-poly","suzanne-low-poly","sword","sword-scimitar","table","taco","tajine","tajine-lid","taxi","tomato","tomato-slice","tower","tractor","tractor-2","tractor-police","tree-big","tree-small","truck","truck-flat","turkey","turntable","utensil-fork","utensil-knife","utensil-spoon","van","waffle","watermelon","whipped-cream","whisk","whole-ham","wholer-ham","wind-turbine","wine-red","wine-white","zombie-1","zombie-2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityArsupportedBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        var adapter = CustomAdapter(modelList)
        Log.i("list", adapter.itemCount.toString())
        viewBinding.ARRecyclerView.adapter = adapter

        viewBinding.bottomNavigation.selectedItemId = R.id.list

        viewBinding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.tech -> {
                    var intent = Intent(applicationContext, TechStackActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true
                }
                R.id.home -> {
                    var intent = Intent(applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true

                }
                R.id.list -> {
                    var intent = Intent(applicationContext, ARSupportedActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    this.finish()
                    true
                }
                else -> {
                    false
                }

            }
        }

    }
}