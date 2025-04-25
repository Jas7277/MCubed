#pragma once

#include <string>

#include "RCONServer.h"
#include "OSSupport/IsThread.h"
#include "OSSupport/Network.h"

#include "mbedTLS++/RsaPrivateKey.h"

namespace Json {
    class Value;
}

class cClientHandle;
class cCommandOutputCallback;
class cSettingsRepositoryInterface;
class cUUID;

class cServer {
    void Shutdown(void);

    void AuthenticateUser(int clientID, std::string && username, const cUUID & uuid, Json::Value && properties);
};