# Java Backend + JavaScript Frontend: Complete Integration Guide

## Table of Contents
1. [Introduction](#introduction)
2. [Communication Methods](#communication-methods)
3. [Java Backend Frameworks](#java-backend-frameworks)
4. [JavaScript Frontend Frameworks](#javascript-frontend-frameworks)
5. [Authentication & Security](#authentication--security)
6. [Deployment Strategies](#deployment-strategies)
7. [Best Practices](#best-practices)
8. [Common Pitfalls](#common-pitfalls)
9. [Real-World Example](#real-world-example)

## Introduction

Modern web applications typically follow a **separation of concerns** architecture where:

- **Java Backend**: Handles business logic, data processing, database operations, and API endpoints
- **JavaScript Frontend**: Manages user interface, user interactions, and client-side logic
- **Communication**: Both sides communicate through well-defined APIs (REST, GraphQL, WebSockets)

### Architecture Overview
```
┌─────────────────┐     HTTP/WebSocket     ┌─────────────────┐
│   Frontend      │ ◄──────────────────► │   Backend       │
│   (JavaScript)  │      JSON/API         │   (Java)        │
│                 │                       │                 │
│ • React/Angular │                       │ • Spring Boot   │
│ • Vue.js/Next.js│                       │ • Quarkus       │
│ • State Mgmt    │                       │ • Micronaut     │
└─────────────────┘                       └─────────────────┘
                                                   │
                                                   ▼
                                          ┌─────────────────┐
                                          │    Database     │
                                          │   (PostgreSQL,  │
                                          │    MongoDB)     │
                                          └─────────────────┘
```

## Communication Methods

### 1. REST APIs (Most Common)

**Java Backend (Spring Boot)**:
```java
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

**JavaScript Frontend (React)**:
```javascript
// API service
const API_BASE_URL = 'http://localhost:8080/api';

export const userService = {
    getAllUsers: async () => {
        const response = await fetch(`${API_BASE_URL}/users`);
        return response.json();
    },
    
    createUser: async (userData) => {
        const response = await fetch(`${API_BASE_URL}/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });
        return response.json();
    },
    
    getUserById: async (id) => {
        const response = await fetch(`${API_BASE_URL}/users/${id}`);
        return response.json();
    }
};

// React component
import React, { useState, useEffect } from 'react';
import { userService } from './services/userService';

function UserList() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const userData = await userService.getAllUsers();
                setUsers(userData);
            } catch (error) {
                console.error('Failed to fetch users:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    if (loading) return <div>Loading...</div>;

    return (
        <div>
            <h2>Users</h2>
            {users.map(user => (
                <div key={user.id}>
                    <h3>{user.name}</h3>
                    <p>{user.email}</p>
                </div>
            ))}
        </div>
    );
}
```

### 2. GraphQL (Flexible Data Fetching)

**Java Backend (Spring Boot + GraphQL)**:
```java
@Component
public class UserResolver implements GraphQLQueryResolver, GraphQLMutationResolver {
    
    @Autowired
    private UserService userService;
    
    public List<User> users() {
        return userService.findAll();
    }
    
    public User user(Long id) {
        return userService.findById(id).orElse(null);
    }
    
    public User createUser(String name, String email) {
        User user = new User(name, email);
        return userService.save(user);
    }
}
```

**JavaScript Frontend (React + Apollo Client)**:
```javascript
import { gql, useQuery, useMutation } from '@apollo/client';

const GET_USERS = gql`
    query GetUsers {
        users {
            id
            name
            email
        }
    }
`;

const CREATE_USER = gql`
    mutation CreateUser($name: String!, $email: String!) {
        createUser(name: $name, email: $email) {
            id
            name
            email
        }
    }
`;

function UserComponent() {
    const { loading, error, data } = useQuery(GET_USERS);
    const [createUser] = useMutation(CREATE_USER);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error.message}</p>;

    return (
        <div>
            {data.users.map(user => (
                <div key={user.id}>
                    <h3>{user.name}</h3>
                    <p>{user.email}</p>
                </div>
            ))}
        </div>
    );
}
```

### 3. WebSockets (Real-time Communication)

**Java Backend (Spring Boot WebSocket)**:
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(), "/chat")
                .setAllowedOrigins("*");
    }
}

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    
    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Broadcast message to all connected clients
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(message);
            }
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
```

**JavaScript Frontend (WebSocket)**:
```javascript
class ChatService {
    constructor() {
        this.socket = null;
        this.messageHandlers = [];
    }
    
    connect() {
        this.socket = new WebSocket('ws://localhost:8080/chat');
        
        this.socket.onopen = () => {
            console.log('Connected to chat');
        };
        
        this.socket.onmessage = (event) => {
            const message = event.data;
            this.messageHandlers.forEach(handler => handler(message));
        };
        
        this.socket.onclose = () => {
            console.log('Disconnected from chat');
        };
    }
    
    sendMessage(message) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(message);
        }
    }
    
    onMessage(handler) {
        this.messageHandlers.push(handler);
    }
    
    disconnect() {
        if (this.socket) {
            this.socket.close();
        }
    }
}

// React component using the chat service
function ChatComponent() {
    const [messages, setMessages] = useState([]);
    const [chatService] = useState(new ChatService());
    
    useEffect(() => {
        chatService.connect();
        chatService.onMessage((message) => {
            setMessages(prev => [...prev, message]);
        });
        
        return () => chatService.disconnect();
    }, []);
    
    const sendMessage = (text) => {
        chatService.sendMessage(text);
    };
    
    return (
        <div>
            <div>
                {messages.map((msg, index) => (
                    <div key={index}>{msg}</div>
                ))}
            </div>
            <input onKeyPress={(e) => {
                if (e.key === 'Enter') {
                    sendMessage(e.target.value);
                    e.target.value = '';
                }
            }} />
        </div>
    );
}
```

## Java Backend Frameworks

### 1. Spring Boot (Most Popular)

**Advantages**:
- Comprehensive ecosystem (Security, Data, Cloud)
- Auto-configuration and convention over configuration
- Excellent community support and documentation
- Production-ready features (Actuator, Monitoring)

**Example Configuration**:
```java
@SpringBootApplication
@EnableJpaRepositories
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    // getters, setters, constructors
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContaining(String name);
}

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
}
```

### 2. Quarkus (Cloud-Native, Fast Startup)

**Advantages**:
- Ultra-fast startup times
- Low memory consumption
- Native compilation with GraalVM
- Container-first approach

**Example**:
```java
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    
    @Inject
    UserService userService;
    
    @GET
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @POST
    public Response createUser(User user) {
        User created = userService.save(user);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        return userService.findById(id)
            .map(user -> Response.ok(user).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
```

### 3. Micronaut (Lightweight, Compile-Time DI)

**Advantages**:
- Compile-time dependency injection
- Fast startup and low memory usage
- Built-in cloud features
- Reflection-free framework

## JavaScript Frontend Frameworks

### 1. React (Component-Based)

**Key Features**:
- Virtual DOM for performance
- Component-based architecture
- Large ecosystem and community
- Excellent for single-page applications

**State Management with API Integration**:
```javascript
// Using React hooks and context for state management
import React, { createContext, useContext, useReducer } from 'react';

const UserContext = createContext();

const userReducer = (state, action) => {
    switch (action.type) {
        case 'FETCH_USERS_START':
            return { ...state, loading: true };
        case 'FETCH_USERS_SUCCESS':
            return { ...state, loading: false, users: action.payload };
        case 'FETCH_USERS_ERROR':
            return { ...state, loading: false, error: action.payload };
        case 'ADD_USER':
            return { ...state, users: [...state.users, action.payload] };
        default:
            return state;
    }
};

export const UserProvider = ({ children }) => {
    const [state, dispatch] = useReducer(userReducer, {
        users: [],
        loading: false,
        error: null
    });

    const fetchUsers = async () => {
        dispatch({ type: 'FETCH_USERS_START' });
        try {
            const users = await userService.getAllUsers();
            dispatch({ type: 'FETCH_USERS_SUCCESS', payload: users });
        } catch (error) {
            dispatch({ type: 'FETCH_USERS_ERROR', payload: error.message });
        }
    };

    const addUser = async (userData) => {
        try {
            const newUser = await userService.createUser(userData);
            dispatch({ type: 'ADD_USER', payload: newUser });
        } catch (error) {
            console.error('Failed to add user:', error);
        }
    };

    return (
        <UserContext.Provider value={{ state, fetchUsers, addUser }}>
            {children}
        </UserContext.Provider>
    );
};

export const useUsers = () => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error('useUsers must be used within UserProvider');
    }
    return context;
};
```

### 2. Angular (Full Framework)

**Key Features**:
- TypeScript by default
- Dependency injection
- Comprehensive tooling
- Two-way data binding

**Service Integration Example**:
```typescript
// user.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
    id: number;
    name: string;
    email: string;
}

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private apiUrl = 'http://localhost:8080/api/users';

    constructor(private http: HttpClient) {}

    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(this.apiUrl);
    }

    createUser(user: Partial<User>): Observable<User> {
        return this.http.post<User>(this.apiUrl, user);
    }

    getUserById(id: number): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/${id}`);
    }
}

// user-list.component.ts
import { Component, OnInit } from '@angular/core';
import { UserService, User } from './user.service';

@Component({
    selector: 'app-user-list',
    template: `
        <div>
            <h2>Users</h2>
            <div *ngIf="loading">Loading...</div>
            <div *ngFor="let user of users">
                <h3>{{ user.name }}</h3>
                <p>{{ user.email }}</p>
            </div>
        </div>
    `
})
export class UserListComponent implements OnInit {
    users: User[] = [];
    loading = true;

    constructor(private userService: UserService) {}

    ngOnInit() {
        this.userService.getUsers().subscribe({
            next: (users) => {
                this.users = users;
                this.loading = false;
            },
            error: (error) => {
                console.error('Failed to load users:', error);
                this.loading = false;
            }
        });
    }
}
```

### 3. Next.js (React with SSR)

**Key Features**:
- Server-side rendering
- Static site generation
- API routes
- File-based routing

**Example with API Integration**:
```javascript
// pages/users/index.js
import { useState, useEffect } from 'react';

export default function UsersPage({ initialUsers }) {
    const [users, setUsers] = useState(initialUsers);

    return (
        <div>
            <h1>Users</h1>
            {users.map(user => (
                <div key={user.id}>
                    <h3>{user.name}</h3>
                    <p>{user.email}</p>
                </div>
            ))}
        </div>
    );
}

// Server-side rendering
export async function getServerSideProps() {
    try {
        const res = await fetch('http://localhost:8080/api/users');
        const users = await res.json();
        
        return {
            props: {
                initialUsers: users
            }
        };
    } catch (error) {
        return {
            props: {
                initialUsers: []
            }
        };
    }
}
```

## Authentication & Security

### JWT Authentication

**Java Backend (Spring Security)**:
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
    
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint())
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
```

**JavaScript Frontend (JWT Storage and Usage)**:
```javascript
// authService.js
class AuthService {
    constructor() {
        this.token = localStorage.getItem('jwt');
    }
    
    async login(credentials) {
        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials)
            });
            
            if (response.ok) {
                const data = await response.json();
                this.token = data.jwt;
                localStorage.setItem('jwt', this.token);
                return true;
            }
            return false;
        } catch (error) {
            console.error('Login failed:', error);
            return false;
        }
    }
    
    logout() {
        this.token = null;
        localStorage.removeItem('jwt');
    }
    
    getAuthHeader() {
        return this.token ? { Authorization: `Bearer ${this.token}` } : {};
    }
    
    isAuthenticated() {
        return !!this.token;
    }
}

// Axios interceptor for automatic token attachment
import axios from 'axios';

const authService = new AuthService();

axios.interceptors.request.use(
    (config) => {
        const authHeader = authService.getAuthHeader();
        if (authHeader.Authorization) {
            config.headers.Authorization = authHeader.Authorization;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            authService.logout();
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);
```

### CORS Configuration

**Java Backend**:
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "https://myapp.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

## Deployment Strategies

### Docker Containerization

**Java Backend Dockerfile**:
```dockerfile
# Multi-stage build for Java application
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**JavaScript Frontend Dockerfile (React)**:
```dockerfile
# Multi-stage build for React application
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

**Docker Compose**:
```yaml
version: '3.8'
services:
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=database
    depends_on:
      - database
    networks:
      - app-network

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "80:80"
    depends_on:
      - backend
    networks:
      - app-network

  database:
    image: postgres:15
    environment:
      POSTGRES_DB: myapp
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - app-network

volumes:
  postgres_data:

networks:
  app-network:
    driver: bridge
```

### Kubernetes Deployment

**Backend Deployment**:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend-deployment
spec:
  replicas: 3
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
      - name: backend
        image: myapp/backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: DB_HOST
          value: postgres-service
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1"
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 15
---
apiVersion: v1
kind: Service
metadata:
  name: backend-service
spec:
  selector:
    app: backend
  ports:
  - port: 8080
    targetPort: 8080
  type: ClusterIP
```

## Best Practices

### 1. API Design
- Use RESTful conventions and consistent naming
- Implement proper HTTP status codes
- Version your APIs (e.g., `/api/v1/users`)
- Use pagination for large datasets
- Implement proper error handling and logging

### 2. Performance Optimization
- **Backend**: Use connection pooling, caching (Redis), and database indexing
- **Frontend**: Implement code splitting, lazy loading, and memoization
- **Communication**: Use compression (gzip) and CDN for static assets

### 3. Security
- Never store sensitive data in frontend code
- Use HTTPS for all communication
- Implement proper input validation on both sides
- Use secure storage for tokens (httpOnly cookies vs localStorage)
- Implement rate limiting and CSRF protection

### 4. Testing Strategy
```java
// Backend testing (Spring Boot)
@SpringBootTest
@AutoConfigureTestDatabase
class UserControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateUser() {
        User user = new User("John Doe", "john@example.com");
        
        ResponseEntity<User> response = restTemplate.postForEntity(
            "/api/users", user, User.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
    }
}
```

```javascript
// Frontend testing (React with Jest and React Testing Library)
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { UserList } from './UserList';
import { userService } from './services/userService';

jest.mock('./services/userService');

test('should display users when loaded', async () => {
    const mockUsers = [
        { id: 1, name: 'John Doe', email: 'john@example.com' },
        { id: 2, name: 'Jane Smith', email: 'jane@example.com' }
    ];
    
    userService.getAllUsers.mockResolvedValue(mockUsers);
    
    render(<UserList />);
    
    await waitFor(() => {
        expect(screen.getByText('John Doe')).toBeInTheDocument();
        expect(screen.getByText('Jane Smith')).toBeInTheDocument();
    });
});
```

## Common Pitfalls

### 1. CORS Issues
**Problem**: Frontend can't access backend APIs due to CORS restrictions.
**Solution**: Configure CORS properly in your backend and use a proxy during development.

### 2. Authentication Token Storage
**Problem**: Storing JWT tokens in localStorage makes them vulnerable to XSS attacks.
**Solution**: Use httpOnly cookies or secure session storage.

### 3. API Versioning
**Problem**: Breaking changes in APIs affect frontend applications.
**Solution**: Implement proper API versioning and maintain backward compatibility.

### 4. Error Handling
**Problem**: Poor error handling leads to bad user experience.
**Solution**: Implement comprehensive error handling on both frontend and backend.

### 5. Performance Issues
**Problem**: N+1 queries, large payloads, or inefficient state management.
**Solution**: Use proper database optimization, pagination, and state management patterns.

## Real-World Example

Here's a complete example of a simple e-commerce product catalog:

### Java Backend (Spring Boot)

```java
// Product Entity
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    private String description;
    private String category;
    private Integer stock;
    
    // constructors, getters, setters
}

// Product Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

// Product Service
@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    public Page<Product> findByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }
    
    public Page<Product> searchByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}

// Product Controller
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;
        
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchByName(search, pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            products = productService.findByCategory(category, pageable);
        } else {
            products = productService.findAll(pageable);
        }
        
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

### JavaScript Frontend (React)

```javascript
// Product Service
export const productService = {
    getProducts: async (page = 0, size = 10, category = '', search = '') => {
        const params = new URLSearchParams({
            page: page.toString(),
            size: size.toString(),
            ...(category && { category }),
            ...(search && { search })
        });
        
        const response = await fetch(`/api/products?${params}`);
        return response.json();
    },
    
    getProduct: async (id) => {
        const response = await fetch(`/api/products/${id}`);
        return response.json();
    }
};

// Product List Component
import React, { useState, useEffect } from 'react';
import { productService } from '../services/productService';

export function ProductList() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [category, setCategory] = useState('');
    const [search, setSearch] = useState('');

    useEffect(() => {
        fetchProducts();
    }, [currentPage, category, search]);

    const fetchProducts = async () => {
        setLoading(true);
        try {
            const data = await productService.getProducts(
                currentPage, 10, category, search
            );
            setProducts(data.content);
            setTotalPages(data.totalPages);
        } catch (error) {
            console.error('Failed to fetch products:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (searchTerm) => {
        setSearch(searchTerm);
        setCurrentPage(0);
    };

    const handleCategoryFilter = (selectedCategory) => {
        setCategory(selectedCategory);
        setCurrentPage(0);
    };

    if (loading) return <div>Loading products...</div>;

    return (
        <div className="product-list">
            <div className="filters">
                <input
                    type="text"
                    placeholder="Search products..."
                    value={search}
                    onChange={(e) => handleSearch(e.target.value)}
                />
                <select
                    value={category}
                    onChange={(e) => handleCategoryFilter(e.target.value)}
                >
                    <option value="">All Categories</option>
                    <option value="electronics">Electronics</option>
                    <option value="clothing">Clothing</option>
                    <option value="books">Books</option>
                </select>
            </div>

            <div className="products">
                {products.map(product => (
                    <div key={product.id} className="product-card">
                        <h3>{product.name}</h3>
                        <p className="price">${product.price}</p>
                        <p className="description">{product.description}</p>
                        <p className="category">Category: {product.category}</p>
                        <p className="stock">Stock: {product.stock}</p>
                    </div>
                ))}
            </div>

            <div className="pagination">
                <button
                    disabled={currentPage === 0}
                    onClick={() => setCurrentPage(currentPage - 1)}
                >
                    Previous
                </button>
                <span>Page {currentPage + 1} of {totalPages}</span>
                <button
                    disabled={currentPage >= totalPages - 1}
                    onClick={() => setCurrentPage(currentPage + 1)}
                >
                    Next
                </button>
            </div>
        </div>
    );
}
```

This comprehensive guide covers the essential aspects of integrating Java backends with JavaScript frontends. The key to success is understanding the communication patterns, choosing the right tools for your specific needs, and following best practices for security, performance, and maintainability.

Remember that the technology landscape evolves rapidly, so stay updated with the latest versions and best practices of the frameworks and tools you choose to use.