-- Seed permissions
INSERT INTO permissions (name, code)
VALUES
    -- Product Admin
    ('Create Product', 'CREATE_PRODUCT'),
    ('Update Product', 'UPDATE_PRODUCT'),
    ('Delete Product', 'DELETE_PRODUCT'),
    ('Update Inventory', 'UPDATE_INVENTORY'),

    -- Category Admin
    ('Create Category', 'CREATE_CATEGORY'),
    ('Update Category', 'UPDATE_CATEGORY'),
    ('Delete Category', 'DELETE_CATEGORY'),
    ('Toggle Category Status', 'TOGGLE_CATEGORY_STATUS'),
    ('View Category', 'VIEW_CATEGORY'),
    ('Check Product Stock','CHECK_PRODUCT_STOCK'),

    -- Customer / General
    ('Update Profile', 'UPDATE_PROFILE'),
    ('View Products', 'VIEW_PRODUCTS'),
    ('View Cart', 'VIEW_CART'),
    ('Add To Cart', 'ADD_TO_CART'),

    -- Payments
    ('Checkout', 'CHECKOUT'),
    ('Payment Success', 'PAYMENT_SUCCESS'),
    ('Payment Cancel', 'PAYMENT_CANCEL'),
    ('Toggle Product Status', 'TOGGLE_PRODUCT_STATUS'),
    ('Upload Product Image', 'UPLOAD_PRODUCT_IMAGE'),
    ('Update Product Image', 'UPDATE_PRODUCT_IMAGE'),

    ('View Product Details', 'VIEW_PRODUCT'),
    ('Search Product', 'SEARCH_PRODUCT'),

    ('Update Cart Item', 'UPDATE_CART'),
    ('Delete Cart', 'DELETE_CART'),
    ('Clear Cart Items', 'CLEAR_CART'),

    ('Update Profile Image', 'UPDATE_PROFILE_IMAGE'),
    ('Upload Profile Image', 'UPLOAD_PROFILE_IMAGE');