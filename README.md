# Network-Services
A group project using Java for Operating Systems Lab (CMPS 405) that shows socket programming 

This project involves the development of a multithreaded Server/Client application capable of serving various network services concurrently that includes DNS and FTP. It uses CLI.

## Server Specifications

1. **24/7 Availability and Handling Multiple Clients**: The server must operate continuously and handle multiple clients simultaneously.
2. **Listening Thread**: It should consistently listen for incoming connections on a separate thread.
3. **Predefined Services**: The server has predefined services including DNS and FTP.

## FTP Service

### Client-Server Interaction

1. **FTP Connection**: Clients can connect to the server using FTP.
2. **Authentication**: Server authenticates client credentials, allowing access only to authorized users.
3. **File Transfer**: Clients can upload and download files to/from the server.

### Additional Features

4. **Concurrent Handling**: The server can handle multiple FTP requests from different clients concurrently using multithreading.
5. **Directory Navigation**: Clients can navigate through the server's directory structure, listing directories and files.
6. **File Compression**: Support for file compression and decompression on the server, reducing transfer time for large files.
7. **Quotas**: Implement file size quotas for client uploads, rejecting uploads exceeding the quota limit.

## DNS Service

- **Implementation**: Implement DNS using the UDP protocol.
- **Functionality**: DNS translates domain names to IP addresses, allowing browsers to load Internet resources. Simulate this service in the server code using UDP sockets.
- **Bidirectional Translation**: The server responds with the corresponding IP address for a domain name sent by the client and vice versa.
  
### Logging

The server logs essential information for connected clients with synchronized access, including:
- Client IP address
- Connection time and duration
- Type of service (DNS, FTP - feature)

These logs aid in troubleshooting and auditing.

### Example
<div style="display: flex;" align="center">
  <img src="https://github.com/Salma-Eletreby/Network-Services/assets/142803990/6e8e1a9f-e605-4715-92a0-a07aab70ec9d" alt="image">
  <img src="https://github.com/Salma-Eletreby/Network-Services/assets/142803990/1c25128a-8212-482b-82f6-05dc027642ac">
</div>
