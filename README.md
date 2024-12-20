# EasyShop Capstone 🛒

## Project Description
EasyShop is an e-commerce platform that allows users to explore and purchase products online. This project focuses on enhancing the backend API for EasyShop, leveraging Spring Boot for the backend server and MySQL for database management. The goal is to improve the existing functionality (Version 1) by fixing bugs and implementing new features for Version 2.

### Project Overview
As a backend developer, you will work on the API that powers the EasyShop platform. You will:
- **Bug Fixes**: Identify and resolve existing issues in the API to ensure smooth functionality.
- **New Features**: Design, develop, and integrate additional features to enhance the platform.
- **Testing**: Use Postman to test API endpoints and ensure seamless integration with the frontend.

#### Tools and Technology 🛠️
- **Backend**: Spring Boot, MySQL
- **Testing**: Postman, Unit Testing
- **Version Control**: GitHub

### Optional Features
To further enhance the EasyShop platform, the following optional features can be developed:

#### Shopping Cart
- Allow users to add, update, and delete items from their cart.

#### User Profile
- When a user registers for an account, a user profile record is automatically created in the profiles table.

#### Checkout
- Allow users to finalize their shopping process by converting their cart into an order.

---

### Key Changes

#### Annotations
- Add various Spring annotations such as `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, and `@DeleteMapping` to define endpoints and ensure correct functionality.

#### Fix Logic in `SqlDao` Classes
- Implement missing logic for methods in the `SqlDao` classes that were defined in their respective interfaces but lacked functionality. Ensure these methods correctly interact with the database and return the expected results.

#### Fix Database Duplicates
- Resolve issues with duplicate entries in the database to ensure data integrity and avoid redundancy.

#### Frontend Price Display Fix
- Corrected an issue in the frontend where the minimum price was displayed twice. Ensured the UI now shows unique and accurate pricing details.

---

### Screenshots

#### Backend

![Screenshot 2024-12-19 194536](https://github.com/user-attachments/assets/a043cf5b-2049-4bc6-a189-497d30c69545)

#### Frontend

![Screenshot 2024-12-19 194710](https://github.com/user-attachments/assets/377fa944-1fe2-4c16-97b0-52a1c7687884)

#### Postman

![Screenshot 2024-12-19 195243](https://github.com/user-attachments/assets/5c6c3889-7c99-426c-9e2e-17be18ddda26)

#### SQL

![image](https://github.com/user-attachments/assets/950daf85-e918-43d0-a9fc-db65327b6fbd)


