#pragma once
#include <memory>
#include <vector>
#include <string>
#include <cstdint>

class cTCPLink;
typedef std::shared_ptr<cTCPLink> cTCPLinkPtr;
typedef std::vector<cTCPLinkPtr> cTCPLinkPtrs;

class cServerHandle;
typedef std::shared_ptr<cServerHandle> cServerHandlePtr;
typedef std::vector<cServerHandlePtr> cServerHandlePtrs;

class cCryptoKey;
typedef std::shared_ptr<cCryptoKey> cCryptoKeyPtr;

class cX509Cert;
typedef std::shared_ptr<cX509Cert> cX509CertPtr;

class cTCPLink {
    friend class cNetwork;

    public:
        class cCallbacks {
            public:
                virtual ~cCallbacks() {}

                virtual void OnLinkCreated(cTCPLinkPtr link) = 0;

                virtual void OnReceivedData(const char * data, size_t length) = 0;

                virtual void OnRemoteClosed(void) = 0;

                virtual void OnTLSHandshakeCompleted(void) {}

                virtual void OnError(int errorCode, const std::string & errorMsg) = 0;
        };
        typedef std::shared_ptr<cCallbacks> cCallbacksPtr;

        virtual ~cTCPLink() {}

        virtual bool Send(const void * data, size_t length) = 0;

        bool Send(const std::string & data) {
            return Send(data.data(), data.size());
        }

        virtual std::string GetLocalIP(void) const = 0;

        virtual std::uint16_t GetLocalPort(void) const = 0;

        virtual std::string GetRemoteIP(void) const = 0;

        virtual std::uint16_t GetRemotePort(void) const = 0;

        virtual void Shutdown(void) = 0;

        virtual void Close(void) = 0;

        virtual std::string StartTLSClient(
            cX509CertPtr ownCert,
            cCryptoKeyPtr ownPrivKey,
            cX509CertPtr trustedRootCAs
        ) = 0;

        virtual std::string StartTLSServer(
            cX509CertPtr ownCert,
            cCryptoKeyPtr ownPrivKey,
            const std::string & startTLSData
        ) = 0;

        cCallbacksPtr GetCallbacks(void) const { return callbacks; }

    protected:
        cCallbacksPtr callbacks;

        cTCPLink(cCallbacksPtr callbacks):
            callbacks(std::move(callbacks))
        {    
        }
};

class cServerHandle {
    friend class cNetwork;

    public:
        virtual ~cServerHandle() {}

        virtual void Close(void) = 0;

        virtual bool IsListening(void) const = 0;
};

class cUDPEndpoint {
    public:
        class cCallbacks {
            virtual ~cCallbacks() {}
            virtual void OnError(int errorCode, const std::string & errorMsg) = 0;
            virtual void OnReceivedData(const char *data, size_t size, const std::string &remoteHost, uint16_t remotePort) = 0;
        };

        virtual ~cUDPEndpoint() {}
        virtual void Close(void) = 0;
        virtual bool IsOpen(void) const = 0;
        virtual uint16_t GetPort(void) const = 0;
        virtual bool Send(const std::string &payload, const std::string &host, uint16_t port) = 0;
        virtual void EnableBroadcasts(void) = 0;

    protected:
        cCallbacks &m_Callbacks;

        cUDPEndpoint(cCallbacks &a_Callbacks):
            m_Callbacks(a_Callbacks)
        {
        }
};
typedef std::shared_ptr<cUDPEndpoint> cUDPEndpointPtr;

class cNetwork {
    public:
        class cConnectCallbacks {
            public:
                virtual ~cConnectCallbacks() {}
                virtual void OnConnected(cTCPLink &link) = 0;
                virtual void OnError(int errorCode, const std::string &errorMsg) = 0;
        };

        typedef std::shared_ptr<cConnectCallbacks> cConnectCallbacksPtr;

        class cListenCallbacks {
            public:
                virtual ~cListenCallbacks() {}
                virtual cTCPLink::cCallbacksPtr OnIncomingConnection(const std::string &remoteIPAddress, uint16_t remotePort) = 0;
                virtual void OnAccepted(cTCPLink &link) = 0;
                virtual void OnError(int errorCode, const std::string &errorMsg) = 0;
        };
        typedef std::shared_ptr<cListenCallbacks> cListenCallbacksPtr;

        class cResolveNameCallbacks {
            public:
                virtual ~cResolveNameCallbacks() {}
                virtual void OnNameResolved(const std::string &name, const std::string &IP) = 0;
                virtual bool OnNameResolvedV4(const std::string &name, const sockaddr_in *IP) {return true;}
                virtual bool OnNameResolvedV6(const std::string &name, const sockaddr_in6 *IP) {return true;}
                virtual void OnError(int errorCode, const std::string &errorMsg) = 0;
                virtual void OnFinished(void) = 0;
        };
        typedef std::shared_ptr<cResolveNameCallbacks> cResolveNameCallbacksPtr;

        static bool Connect(
            const std::string &host,
            uint16_t port,
            cConnectCallbacksPtr connectCallbacks,
            cTCPLink::cCallbacksPtr linkCallbacks
        );

        static cServerHandlePtr Listen(
            uint16_t port,
            cListenCallbacksPtr listenCallbacks
        );

        static bool HostnameToIP(
            const std::string &hostname,
            cResolveNameCallbacksPtr callbacks
        );

        static bool IPToHostName(
            const std::string &IP,
            cResolveNameCallbacksPtr callbacks
        );

        static cUDPEndpointPtr CreateUDPEndpoint(uint16_t port, cUDPEndpoint::cCallbacks &callbacks);

        static std::vector<std::string> EnumLocalIPAddresses(void);
};