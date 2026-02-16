package com.github.luidsonl.inventory_manager_api.bootstrap;

import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;
import com.github.luidsonl.inventory_manager_api.enums.TransactionType;
import com.github.luidsonl.inventory_manager_api.model.*;
import com.github.luidsonl.inventory_manager_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final RawMaterialRepository rawMaterialRepository;
    private final RawMaterialPackagingRepository rawMaterialPackagingRepository;
    private final ProductRepository productRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final ProductTransactionRepository productTransactionRepository;
    private final RawMaterialPackagingTransactionRepository rawMaterialPackagingTransactionRepository;

    @Override
    public void run(String... args) {
        if (rawMaterialRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        // Raw Materials
        RawMaterial wheatFlour = new RawMaterial(null, "RM001", "Wheat Flour", "High protein wheat flour",
                MeasureUnitsType.KILOGRAM, new ArrayList<>(), true);
        RawMaterial sugar = new RawMaterial(null, "RM002", "White Sugar", "Refined white sugar",
                MeasureUnitsType.KILOGRAM, new ArrayList<>(), true);
        RawMaterial water = new RawMaterial(null, "RM003", "Water", "Filtered water", MeasureUnitsType.LITER,
                new ArrayList<>(), true);
        RawMaterial yeast = new RawMaterial(null, "RM004", "Yeast", "Dry instant yeast", MeasureUnitsType.GRAM,
                new ArrayList<>(), true);
        RawMaterial salt = new RawMaterial(null, "RM005", "Salt", "Sea salt", MeasureUnitsType.GRAM, new ArrayList<>(),
                true);
        RawMaterial oil = new RawMaterial(null, "RM006", "Vegetable Oil", "Soybean oil", MeasureUnitsType.LITER,
                new ArrayList<>(), true);

        rawMaterialRepository.saveAll(Arrays.asList(wheatFlour, sugar, water, yeast, salt, oil));

        // Packagings
        RawMaterialPackaging flourBag = new RawMaterialPackaging(null, "50kg Bag", wheatFlour,
                new BigDecimal("50.0000"), 10);
        RawMaterialPackaging sugarPack = new RawMaterialPackaging(null, "1kg Pack", sugar, new BigDecimal("1.0000"),
                20);
        RawMaterialPackaging oilBottle = new RawMaterialPackaging(null, "900ml Bottle", oil, new BigDecimal("0.9000"),
                15);
        RawMaterialPackaging saltPack = new RawMaterialPackaging(null, "1kg Pack", salt, new BigDecimal("1.0000"), 5);

        rawMaterialPackagingRepository.saveAll(Arrays.asList(flourBag, sugarPack, oilBottle, saltPack));

        // Initial Stock Transactions for Raw Materials
        rawMaterialPackagingTransactionRepository.saveAll(Arrays.asList(
                new RawMaterialPackagingTransaction(null, flourBag, 10, TransactionType.INVENTORY_IN, null,
                        "Initial stock for development"),
                new RawMaterialPackagingTransaction(null, sugarPack, 20, TransactionType.INVENTORY_IN, null,
                        "Initial stock for development"),
                new RawMaterialPackagingTransaction(null, oilBottle, 15, TransactionType.INVENTORY_IN, null,
                        "Initial stock for development")));

        // Products
        Product artisanBread = new Product(null, "P001", "Artisan Bread", new BigDecimal("5.50"), false,
                new BigDecimal("10.0000"), new ArrayList<>());
        Product sweetCake = new Product(null, "P002", "Sweet Cake", new BigDecimal("15.00"), false,
                new BigDecimal("5.0000"), new ArrayList<>());
        Product chocolateCookies = new Product(null, "P003", "Chocolate Cookies", new BigDecimal("8.00"), false,
                new BigDecimal("20.0000"), new ArrayList<>());

        productRepository.saveAll(Arrays.asList(artisanBread, sweetCake, chocolateCookies));

        // Product Transactions (Initial production logs)
        productTransactionRepository.saveAll(Arrays.asList(
                new ProductTransaction(null, artisanBread, new BigDecimal("10.0000"), TransactionType.INVENTORY_IN,
                        null,
                        "Initial production log"),
                new ProductTransaction(null, sweetCake, new BigDecimal("5.0000"), TransactionType.INVENTORY_IN, null,
                        "Initial production log"),
                new ProductTransaction(null, chocolateCookies, new BigDecimal("20.0000"), TransactionType.INVENTORY_IN,
                        null, "Initial production log")));

        // ProductRawMaterials (Recipe)
        // Bread: 0.5kg flour, 0.3L water, 10g yeast, 5g salt
        productRawMaterialRepository.saveAll(Arrays.asList(
                new ProductRawMaterial(null, artisanBread, wheatFlour, new BigDecimal("0.5000")),
                new ProductRawMaterial(null, artisanBread, water, new BigDecimal("0.3000")),
                new ProductRawMaterial(null, artisanBread, yeast, new BigDecimal("10.0000")),
                new ProductRawMaterial(null, artisanBread, salt, new BigDecimal("5.0000"))));

        // Cake: 0.3kg flour, 0.2kg sugar, 0.1L water, 0.05L oil
        productRawMaterialRepository.saveAll(Arrays.asList(
                new ProductRawMaterial(null, sweetCake, wheatFlour, new BigDecimal("0.3000")),
                new ProductRawMaterial(null, sweetCake, sugar, new BigDecimal("0.2000")),
                new ProductRawMaterial(null, sweetCake, water, new BigDecimal("0.1000")),
                new ProductRawMaterial(null, sweetCake, oil, new BigDecimal("0.0500"))));

        // Cookies: 0.2kg flour, 0.1kg sugar, 0.05kg oil
        productRawMaterialRepository.saveAll(Arrays.asList(
                new ProductRawMaterial(null, chocolateCookies, wheatFlour, new BigDecimal("0.2000")),
                new ProductRawMaterial(null, chocolateCookies, sugar, new BigDecimal("0.1000")),
                new ProductRawMaterial(null, chocolateCookies, oil, new BigDecimal("0.0500"))));

        System.out.println("Database seeded successfully with expanded development data!");
    }
}
