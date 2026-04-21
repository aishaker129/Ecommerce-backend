-- SUPER_ADMIN gets ALL permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         CROSS JOIN permissions p
WHERE r.code = 'SUPER_ADMIN';


-- ADMIN gets full product + category + payment control (no user/cart management)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.code IN (
                                          'CREATE_PRODUCT','UPDATE_PRODUCT','UPDATE_INVENTORY','UPLOAD_PRODUCT_IMAGE',
                                          'UPDATE_PRODUCT_IMAGE','UPDATE_PROFILE','VIEW_PRODUCT','VIEW_PRODUCTS',
                                          'VIEW_CATEGORY','VIEW_CART','ADD_TO_CART','SEARCH_PRODUCT','UPDATE_CART',
                                          'DELETE_CART','CLEAR_CART','UPDATE_PROFILE_IMAGE','UPLOAD_PROFILE_IMAGE','CHECKOUT',
                                          'PAYMENT_SUCCESS','PAYMENT_CANCEL','DELETE_PRODUCT','TOGGLE_PRODUCT_STATUS','CREATE_CATEGORY',
                                          'UPDATE_CATEGORY','DELETE_CATEGORY','TOGGLE_CATEGORY_STATUS','CHECK_PRODUCT_STOCK','CHECK_PRODUCT_STOCK'
    )
WHERE r.code = 'ADMIN';


-- SELLER gets limited product management + inventory + view access
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.code IN (
                                          'CREATE_PRODUCT','UPDATE_PRODUCT','UPDATE_INVENTORY','UPLOAD_PRODUCT_IMAGE',
                                          'UPDATE_PRODUCT_IMAGE','UPDATE_PROFILE','VIEW_PRODUCT','VIEW_PRODUCTS',
                                          'VIEW_CATEGORY','VIEW_CART','ADD_TO_CART','SEARCH_PRODUCT','UPDATE_CART',
                                          'DELETE_CART','CLEAR_CART','UPDATE_PROFILE_IMAGE','UPLOAD_PROFILE_IMAGE',
                                          'CHECKOUT','PAYMENT_SUCCESS','PAYMENT_CANCEL','CHECK_PRODUCT_STOCK','CHECK_PRODUCT_STOCK'
    )
WHERE r.code = 'SELLER';


-- CUSTOMER gets shopping + cart + payment + profile permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON p.code IN (
                                          'UPDATE_PROFILE',
                                          'VIEW_PRODUCTS',
                                          'VIEW_PRODUCT',
                                          'VIEW_CATEGORY',
                                          'VIEW_CART',
                                          'ADD_TO_CART',
                                          'UPDATE_CART',
                                          'DELETE_CART',
                                          'CLEAR_CART',
                                          'CHECKOUT',
                                          'PAYMENT_SUCCESS',
                                          'PAYMENT_CANCEL',
                                          'UPDATE_PROFILE_IMAGE',
                                          'UPLOAD_PROFILE_IMAGE'
    )
WHERE r.code = 'CUSTOMER';