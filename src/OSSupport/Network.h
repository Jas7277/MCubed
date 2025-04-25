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